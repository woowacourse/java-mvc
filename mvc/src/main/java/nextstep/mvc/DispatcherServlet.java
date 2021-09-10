package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import nextstep.core.ApplicationContext;
import nextstep.mvc.adapter.ControllerImplementHandlerAdapter;
import nextstep.mvc.adapter.HandlerAdapter;
import nextstep.mvc.adapter.RequestMappingHandlerAdapter;
import nextstep.mvc.mapping.AnnotationHandlerMapping;
import nextstep.mvc.exceptionresolver.ExceptionResolverContainer;
import nextstep.mvc.mapping.ControllerImplementHandlerMapping;
import nextstep.mvc.mapping.HandlerMapping;
import nextstep.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final ApplicationContext applicationContext;
    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;
    private final StaticResourceHandler staticResourceHandler;
    private final ExceptionResolverContainer exceptionResolverContainer;

    public DispatcherServlet(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.handlerMappings = new ArrayList<>();
        this.handlerAdapters = new ArrayList<>();
        this.staticResourceHandler = new DefaultStaticResourceHandler();
        this.exceptionResolverContainer = new ExceptionResolverContainer();
    }

    @Override
    public void init() {
        defaultHandlerMappings();
        defaultHandlerAdapters();

        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    private void defaultHandlerMappings() {
        handlerMappings.add(new AnnotationHandlerMapping(applicationContext));
        handlerMappings.add(new ControllerImplementHandlerMapping(applicationContext));
    }

    private void defaultHandlerAdapters() {
        handlerAdapters.add(new RequestMappingHandlerAdapter());
        handlerAdapters.add(new ControllerImplementHandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final Object handler = findHandler(request);
            if (handler == null) {
                staticResourceHandle(request, response);
                return;
            }
            ModelAndView mv = resolveHandler(request, response, handler);
            mv.render(request, response);
        } catch (Exception e) {
            resolveError(request, response, e);
        }
    }

    private Object findHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            final Object handler = handlerMapping.getHandler(request);
            if(handler != null) return handler;
        }
        return null;
    }

    private ModelAndView resolveHandler(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        return handlerAdapters.stream().filter(adapter -> adapter.supports(handler))
            .findAny()
            .orElseThrow(() -> new IllegalStateException("not supported handler type"))
            .handle(request, response, handler);
    }

    private void staticResourceHandle(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        staticResourceHandler.handleResource(httpRequest, httpResponse);
    }

    private void resolveError(HttpServletRequest request, HttpServletResponse response,
                              Exception exception) throws ServletException {
        exceptionResolverContainer.resolve(exception, request, response);
    }
}
