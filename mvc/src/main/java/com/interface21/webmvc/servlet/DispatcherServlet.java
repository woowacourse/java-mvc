package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.handler.adapter.HandlerAdapters;
import com.interface21.webmvc.servlet.mvc.handler.mapping.HandlerMappings;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappings handlerMappings;
    private final HandlerAdapters handlerAdapters;

    public DispatcherServlet(HandlerMappings handlerMappings, HandlerAdapters handlerAdapters) {
        this.handlerMappings = handlerMappings;
        this.handlerAdapters = handlerAdapters;
    }

    @Override
    public void init() {
        handlerMappings.init();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final var handler = handlerMappings.getHandler(request);
            final var modelAndView = handlerAdapters.handle(request, response, handler);
            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
