package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.tobe.handleradapter.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.handleradapter.ControllerHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.handleradapter.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.handlermapping.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.handlermapping.ControllerHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.handlermapping.HandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    public static final String BASE_PACKAGE = "com.techcourse.controller";


    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();
    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(BASE_PACKAGE);
        final ControllerHandlerMapping controllerHandlerMapping = new ControllerHandlerMapping(BASE_PACKAGE);
        annotationHandlerMapping.initialize();
        controllerHandlerMapping.initialize();
        handlerMappings.add(annotationHandlerMapping);
        handlerMappings.add(controllerHandlerMapping);

        handlerAdapters.add(new ControllerHandlerAdapter());
        handlerAdapters.add(new AnnotationHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = getHandler(request);
            final HandlerAdapter adapter = getAdapter(handler);
            final ModelAndView modelAndView = adapter.handle(request, response, handler);
            render(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            try {
                return handlerMapping.getHandler(request);
            } catch (Exception ignored) {
            }
        }
        throw new IllegalArgumentException("404");
    }

    private HandlerAdapter getAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.isSupport(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("예기치 못한 에러"));
    }

    private void render(final ModelAndView modelAndView, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final View view = modelAndView.getView();
        final Map<String, Object> model = modelAndView.getModel();
        view.render(model, request, response);
    }
}
