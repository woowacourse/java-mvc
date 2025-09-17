package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.adapter.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.adapter.HandlerExecutionAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        initHandlerMappings();
        initHandlerAdapters();
    }

    private void initHandlerMappings() {
        handlerMappings = new ArrayList<>();

        ManualHandlerMapping manualMapping = new ManualHandlerMapping();
        manualMapping.initialize();
        handlerMappings.add(manualMapping);

        AnnotationHandlerMapping annotationMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        annotationMapping.initialize();
        handlerMappings.add(annotationMapping);
    }

    private void initHandlerAdapters() {
        handlerAdapters = new ArrayList<>();
        handlerAdapters.add(new ControllerHandlerAdapter());
        handlerAdapters.add(new HandlerExecutionAdapter());
    }

    @Override
    protected void service(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            Object handler = getHandler(request);
            if (handler == null) {
                return;
            }

            HandlerAdapter adapter = getHandlerAdapter(handler);
            ModelAndView modelAndView = adapter.handle(request, response, handler);

            processView(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(HttpServletRequest request) {
        for (HandlerMapping mapping : handlerMappings) {
            Object handler = mapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        return null;
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        for (HandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new IllegalArgumentException("No adapter for handler: " + handler);
    }

    private void processView(
            ModelAndView modelAndView,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        View view = modelAndView.getView();
        Map<String, Object> model = modelAndView.getModel();

        view.render(model, request, response);
    }
}
