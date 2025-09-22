package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.HandlerMappingRegistry;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet() {
        this.handlerMappingRegistry = HandlerMappingRegistry.createEmpty();
        this.handlerAdapterRegistry = HandlerAdapterRegistry.createEmpty();
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    @Override
    public void init() {
        handlerMappingRegistry.initialize();
    }


    @Override
    protected void service(
            final HttpServletRequest request, final HttpServletResponse response
    ) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            var handler = handlerMappingRegistry.getHandler(request);
            var modelAndView = executeHandler(handler, request, response);
            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private ModelAndView executeHandler(
            final Object handler,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        var handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
        return handlerAdapter.handle(handler, request, response);
    }
}
