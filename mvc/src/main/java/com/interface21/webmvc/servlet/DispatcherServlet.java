package com.interface21.webmvc.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @Override
    public void init() {
        handlerMappingRegistry.initialize();
        handlerAdapterRegistry.initialize();
    }

    public void addHandlerMapping(HandlerMapping mapping) {
        handlerMappingRegistry.addHandlerMapping(mapping);
    }

    public void addHandlerAdapter(HandlerAdapter adapter) {
        handlerAdapterRegistry.addHandlerAdapter(adapter);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            Object handler = getHandler(request);
            HandlerAdapter adapter = getHandlerAdapter(handler);

            ModelAndView modelAndView = adapter.handle(request, response, handler);
            renderModelAndView(request, response, modelAndView);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void renderModelAndView(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView)
            throws Exception {
        Map<String, Object> model = modelAndView.getModel();
        View view = modelAndView.getView();
        view.render(model, request, response);
    }

    private Object getHandler(HttpServletRequest request) {
        return handlerMappingRegistry.getHandler(request)
                .orElseThrow(NotFoundHandlerException::new);
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapterRegistry.getHandlerAdapter(handler)
                .orElseThrow(NotFoundAdapterException::new);
    }
}
