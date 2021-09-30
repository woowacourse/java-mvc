package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.adapter.HandlerAdapter;
import nextstep.mvc.registry.HandlerAdapterRegistry;
import nextstep.mvc.registry.HandlerMappingRegistry;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet() {
        this.handlerMappingRegistry = new HandlerMappingRegistry();
        this.handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @Override
    public void init() {
        handlerMappingRegistry.initializeHandlerMappings();
    }

    public void addToHandlerMappingRegistry(HandlerMapping... handlerMappings) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerMappingRegistry.addHandlerMapping(handlerMapping);
        }
    }

    public void addToHandlerAdapterRegistry(HandlerAdapter... handlerAdapters) {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            Object handler = handler(request);
            HandlerAdapter handlerAdapter = handlerAdapter(handler);
            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            View view = modelAndView.view();
            view.render(modelAndView.model(), request, response);
        } catch (Exception e) {
            log.error("{} : {}", e.getClass(), e.getMessage());
            throw new ServletException(e.getMessage());
        }
    }

    private HandlerAdapter handlerAdapter(Object handler) {
        return handlerAdapterRegistry.findHandlerAdapter(handler);
    }

    private Object handler(HttpServletRequest request) {
        return handlerMappingRegistry.findHandler(request);
    }
}
