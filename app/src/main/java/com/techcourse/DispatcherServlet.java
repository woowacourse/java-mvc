package com.techcourse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    @Override
    public void init() {
        handlerAdapters = new ArrayList<>();
        handlerAdapters.add(new ManualHandlerAdapter());
        handlerAdapters.add(new AnnotationHandlerAdapter());

        handlerMappings = new ArrayList<>();
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        annotationHandlerMapping.initialize();
        handlerMappings.add(manualHandlerMapping);
        handlerMappings.add(annotationHandlerMapping);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            Object handler = getHandler(request);
            HandlerAdapter handlerAdapter = getHandlerAdapter(handler, request);
            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            render(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(HttpServletRequest request) throws ServletException {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() ->
                        new ServletException("No handler found for request URI: " + request.getRequestURI()));
    }

    private HandlerAdapter getHandlerAdapter(Object handler, HttpServletRequest request) throws ServletException {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.support(handler))
                .findFirst()
                .orElseThrow(() ->
                        new ServletException("No handlerAdapter found for request URI: " + request.getRequestURI()));
    }

    private void render(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Map<String, Object> model = modelAndView.getModel();
        View view = modelAndView.getView();
        view.render(model, request, response);
    }
}
