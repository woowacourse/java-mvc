package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Optional;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet(HandlerMappingRegistry handlerMappingRegistry,
                             HandlerAdapterRegistry handlerAdapterRegistry) {
        this.handlerMappingRegistry = handlerMappingRegistry;
        this.handlerAdapterRegistry = handlerAdapterRegistry;
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        Optional<Object> handler;
        try {
            handler = handlerMappingRegistry.getHandler(request);
        } catch (MethodNotAllowedException e) {
            response.setHeader("Allow", e.getAllowHeader());
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        if (handler.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            log.error("No handler found for request URI : {}", requestURI);
            return;
        }

        Object foundHandler = handler.get();
        try {
            HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(foundHandler);
            ModelAndView modelAndView = handlerAdapter.handle(foundHandler, request, response);
            modelAndView.getView().render(modelAndView.getModel(), request, response);

        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e);
        }
    }
}
