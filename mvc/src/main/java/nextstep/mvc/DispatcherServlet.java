package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.adapter.HandlerAdapter;
import nextstep.mvc.adapter.tobe.AnnotationHandlerAdapter;
import nextstep.mvc.mapping.HandlerMapping;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import nextstep.mvc.viewresolver.JsonViewResolver;
import nextstep.mvc.viewresolver.JspViewResolver;
import nextstep.mvc.viewresolver.UrlBasedViewResolver;
import nextstep.mvc.viewresolver.ViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        initialHandlerAdapters();
        initialViewResolver();
    }

    private void initialHandlerAdapters() {
        this.handlerAdapters.add(new AnnotationHandlerAdapter());
    }

    private void initialViewResolver() {
        this.viewResolvers.add(new JsonViewResolver());
        this.viewResolvers.add(new UrlBasedViewResolver());
        this.viewResolvers.add(new JspViewResolver());
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            Object handler = getHandler(request);

            if (Objects.isNull(handler)) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            HandlerAdapter adapter = getHandlerAdapter(handler);

            ModelAndView mv = adapter.handle(request, response, handler);
            render(mv, request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        for (HandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        log.error("Get Handler Adapter fail! : {}", handler);
        throw new IllegalStateException("handler adapter를 찾을 수 없습니다. handler " + handler);
    }

    private void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String viewName = mv.getViewName();
        ViewResolver resolver = viewResolvers.stream()
                .filter(viewResolver -> viewResolver.supports(viewName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("ViewResolver를 찾을 수 없습니다."));
        View view = resolver.resolveViewName(viewName);
        view.render(mv.getModel(), request, response);
    }
}
