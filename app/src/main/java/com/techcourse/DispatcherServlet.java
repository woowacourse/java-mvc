package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.adapter.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.adapter.HandlerAdapters;
import webmvc.org.springframework.web.servlet.mvc.tobe.mapping.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.mapping.HandlerMapping;

import java.util.ArrayList;
import java.util.List;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();
    private final HandlerAdapters handlerAdapters;

    public DispatcherServlet(HandlerAdapters handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    @Override
    public void init() {
        initHandlerMappings();
    }

    private void initHandlerMappings() {
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        manualHandlerMapping.initialize();
        annotationHandlerMapping.initialize();

        handlerMappings.add(manualHandlerMapping);
        handlerMappings.add(annotationHandlerMapping);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            Object handler = getHandler(request);
            HandlerAdapter adapter = handlerAdapters.getHandlerAdapter(handler);
            render(request, response, handler, adapter);

        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(HttpServletRequest request) throws ServletException {
        Object handler = null;
        for (HandlerMapping handlerMapping : handlerMappings) {
            handler = handlerMapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        throw new ServletException("Not found handler for request URI : " + request.getRequestURI());
    }

    private void render(HttpServletRequest request, HttpServletResponse response, Object handler, HandlerAdapter adapter) throws Exception {
        ModelAndView modelAndView = adapter.handle(request, response, handler);
        modelAndView.getView().render(modelAndView.getModel(), request, response);
    }
}
