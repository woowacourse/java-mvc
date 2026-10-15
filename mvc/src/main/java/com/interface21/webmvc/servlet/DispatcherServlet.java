package com.interface21.webmvc.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet(final List<HandlerMapping> handlerMappings, final List<HandlerAdapter> handlerAdapters) {
        this.handlerMappings = List.copyOf(handlerMappings);
        this.handlerAdapters = List.copyOf(handlerAdapters);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final var modelAndView = handle(request, response);
            if (modelAndView == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e);
        }
    }

    private ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        for (final var handlerMapping : handlerMappings) {
            final var handler = handlerMapping.getHandler(request);
            if (handler == null) {
                continue;
            }
            for (final var handlerAdapter : handlerAdapters) {
                if (handlerAdapter.supports(handler)) {
                    return handlerAdapter.handle(request, response, handler);
                }
            }
            throw new IllegalStateException("No adapter for handler: " + handler);
        }
        return null;
    }
}
