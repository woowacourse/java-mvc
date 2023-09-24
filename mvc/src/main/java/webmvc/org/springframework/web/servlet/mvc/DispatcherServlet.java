package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerNotFoundException;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;
    private final WebApplicationContext webApplicationContext;

    public DispatcherServlet(final WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
        this.handlerMappings = new ArrayList<>();
        this.handlerAdapters = new ArrayList<>();
    }

    @Override
    public void init() {
        initHandlerMappings();
        initHandlerAdapters();
    }

    private void initHandlerMappings() {
        handlerMappings.addAll(webApplicationContext.getBeansBySuperType(HandlerMapping.class));
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    private void initHandlerAdapters() {
        handlerAdapters.addAll(webApplicationContext.getBeansBySuperType(HandlerAdapter.class));
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        final var handler = getHandler(request);
        final var handlerAdapter = getHandlerAdapter(handler);
        try {
            final var modelAndView = handlerAdapter.handle(request, response, handler);
            renderView(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                              .map(mapping -> mapping.getHandler(request))
                              .filter(Objects::nonNull)
                              .findAny()
                              .orElseThrow(() -> new HandlerNotFoundException("Not found handler for request URI : " + request.getRequestURI()));
    }

    private HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                              .filter(adapter -> adapter.supports(handler))
                              .findAny()
                              .orElseThrow(() -> new HandlerNotFoundException("Not found handler adapter for handler : " + handler));
    }

    private void renderView(final ModelAndView mv, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final var view = mv.getView();
        view.render(mv.getModel(), request, response);
    }
}
