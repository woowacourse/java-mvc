package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.ControllerAdopter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdopter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdopters;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutionAdopter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMappings;
import webmvc.org.springframework.web.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappings handlerMappings;
    private final HandlerAdopters handlerAdopters;

    public DispatcherServlet() {
        this(new HandlerMappings(), new HandlerAdopters());
    }

    public DispatcherServlet(final HandlerMappings handlerMappings, final HandlerAdopters handlerAdopters) {
        this.handlerMappings = handlerMappings;
        this.handlerAdopters = handlerAdopters;
    }

    @Override
    public void init() {
        handlerMappings.add(new ManualHandlerMapping());
        handlerMappings.add(new AnnotationHandlerMapping(
                "com.techcourse.controller"));
        handlerAdopters.add(new HandlerExecutionAdopter());
        handlerAdopters.add(new ControllerAdopter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = handlerMappings.getHandler(request);
            final HandlerAdopter adopter = handlerAdopters.getAdopter(handler);
            final ModelAndView modelAndView = adopter.handle(handler, request, response);
            move(modelAndView.getView().getViewName(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(final String viewName, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
