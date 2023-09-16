package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.HandlerAdapter;
import webmvc.org.springframework.web.servlet.HandlerMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        this.handlerAdapters = new ArrayList<>();
        this.handlerMappings = new ArrayList<>();

        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        handlerMappings.add(manualHandlerMapping);
        handlerAdapters.add(new ManualHandlerAdapter());

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com");
        annotationHandlerMapping.initialize();
        handlerMappings.add(annotationHandlerMapping);
        handlerAdapters.add(new AnnotationHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            Object handler = getHandler(request);

            ModelAndView modelAndView = getHandlerAdapter(handler, request, response);
            View view = modelAndView.getView();
            Map<String, Object> model = modelAndView.getModel();
            view.render(model, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            Object handler = handlerMapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        throw new RuntimeException();
    }

    private ModelAndView getHandlerAdapter(final Object handler, final HttpServletRequest request,
                                           final HttpServletResponse response) throws Exception {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.support(handler)) {
                return handlerAdapter.handle(handler, request, response);
            }
        }
        throw new RuntimeException();
    }
}
