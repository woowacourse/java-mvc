package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.ControllerHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapters;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutionAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMappings;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappings handlerMappings;
    private final HandlerAdapters handlerAdapters;

    public DispatcherServlet() {
        this.handlerMappings = new HandlerMappings();
        this.handlerAdapters = new HandlerAdapters();
    }

    @Override
    public void init() {
        handlerMappings.addHandlerMapping(new AnnotationHandlerMapping());
        handlerMappings.addHandlerMapping(new ManualHandlerMapping());
        handlerAdapters.addHandlerAdapter(new HandlerExecutionAdapter());
        handlerAdapters.addHandlerAdapter(new ControllerHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final var handler = handlerMappings.getHandler(request);
            final var handlerAdapter = handlerAdapters.getAdapterByHandler(handler);
            final var modelAndView = handlerAdapter.handle(request, response, handler);
            move(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(final ModelAndView modelAndView, final HttpServletRequest request,
                      final HttpServletResponse response) throws Exception {
        modelAndView.getView().render(modelAndView.getModel(), request, response);
    }
}
