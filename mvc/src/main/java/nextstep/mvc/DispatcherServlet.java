package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.registry.HandlerAdapterRegistry;
import nextstep.mvc.registry.HandlerMappingRegistry;
import nextstep.mvc.registry.ModelAndViewResolverRegistry;
import nextstep.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;
    private final ModelAndViewResolverRegistry modelAndViewResolverRegistry;


    public DispatcherServlet() {
        this.handlerMappingRegistry = new HandlerMappingRegistry();
        this.handlerAdapterRegistry = new HandlerAdapterRegistry();
        this.modelAndViewResolverRegistry = new ModelAndViewResolverRegistry();
    }

    @Override
    public void init() {
        handlerMappingRegistry.initialize();
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    public void addModelAndViewResolver(ModelAndViewResolver modelAndViewResolver) {
        modelAndViewResolverRegistry.addModelAndViewResolver(modelAndViewResolver);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            Object handleResult = handleRequest(request, response);
            render(request, response, handleResult);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object handler = findHandler(request);
        HandlerAdapter adapter = findAdapter(handler);

        return adapter.handle(request, response, handler);
    }

    private Object findHandler(final HttpServletRequest request) {
        return handlerMappingRegistry.findHandler(request);
    }

    private HandlerAdapter findAdapter(Object handler) {
        return handlerAdapterRegistry.findAdapter(handler);
    }

    private void render(HttpServletRequest request, HttpServletResponse response, Object handleResult) {
        ModelAndViewResolver resolver = findModelAndViewResolver(handleResult);
        ModelAndView modelAndView = resolver.resolve(handleResult);
        modelAndView.render(request, response);
    }

    private ModelAndViewResolver findModelAndViewResolver(Object handleResult) {
        return modelAndViewResolverRegistry.findModelAndViewResolver(handleResult);
    }
}
