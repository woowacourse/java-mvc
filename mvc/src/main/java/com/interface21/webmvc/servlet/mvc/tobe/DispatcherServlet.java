package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerRegistry handlerRegistry;
    private final ViewResolver viewResolver;

    public DispatcherServlet(HandlerRegistry handlerRegistry, ViewResolver viewResolver) {
        this.handlerRegistry = handlerRegistry;
        this.viewResolver = viewResolver;
    }

    @Override
    public void init() {
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.info("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        try {
            log.info("handlerRegistry = {}", handlerRegistry);
            Object modelAndView = handlerRegistry.handle(request, response);
            viewResolver.resolveView(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Error while handling request: {}", e.getMessage(), e);
            throw new ServletException("Error while processing request", e);
        }
    }
}
