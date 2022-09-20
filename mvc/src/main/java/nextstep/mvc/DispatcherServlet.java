package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import nextstep.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;
    private final List<ViewResolver> viewResolvers;

    public DispatcherServlet() {
        this.handlerMappings = new ArrayList<>();
        this.handlerAdapters = new ArrayList<>();
        this.viewResolvers = new ArrayList<>();
    }

    @Override
    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public void addViewResolvers(ViewResolver viewResolver) {
        this.viewResolvers.add(viewResolver);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final Object handler = findHandler(request);
            final HandlerAdapter adapter = findAdapter(handler);
            ModelAndView modelAndView = adapter.handle(request, response, handler);
            resolve(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object findHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Failed to find handler : " + request.getRequestURI() + " " + request.getMethod()));
    }

    private HandlerAdapter findAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Failed to find adapter : " + handler.getClass()));
    }

    private void resolve(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) {
        try {
            modelAndView.render(request, response);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Unexpected exception occured while rendering", e);
        }
    }

    private ViewResolver findViewResolver(String viewName) {
        return viewResolvers.stream()
                .filter(resolver -> resolver.supports(viewName))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Failed to find viewResolver : " + viewName));
    }
}
