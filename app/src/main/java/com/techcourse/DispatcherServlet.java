package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        addHandlerMapping();
        addHandlerAdapter();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) {
        Object handler = getHandler(request);
        ModelAndView modelAndView = executeHandler(request, response, handler);
        render(modelAndView, request, response);
    }

    private void addHandlerMapping() {
        final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com.interface21",
                "com.techcourse");
        annotationHandlerMapping.initialize();

        handlerMappings.add(manualHandlerMapping);
        handlerMappings.add(annotationHandlerMapping);
    }

    private void addHandlerAdapter() {
        final ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();
        final AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();
        handlerAdapters.add(controllerHandlerAdapter);
        handlerAdapters.add(annotationHandlerAdapter);
    }

    private Object getHandler(HttpServletRequest request) {
        for (var handlerMapping : handlerMappings) {
            Object handler;
            if ((handler = handlerMapping.getHandler(request)) != null) {
                return handler;
            }
        }
        throw new IllegalArgumentException("handler does not exists");
    }

    private ModelAndView executeHandler(HttpServletRequest request, HttpServletResponse response,
                                        Object handler) {
        ModelAndView modelAndView;
        try {
            for (var handlerAdapter : handlerAdapters) {
                if (handlerAdapter.supports(handler)) {
                    modelAndView = handlerAdapter.handle(request, response, handler);
                    return modelAndView;
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("exception occured" + e.getMessage());
        }

        throw new IllegalArgumentException("No HandlerAdapter found for handler: " + handler);
    }

    private void render(final ModelAndView modelAndView, final HttpServletRequest request,
                        final HttpServletResponse response) {
        final Map<String, Object> model = modelAndView.getModel();
        final View view = modelAndView.getView();

        try {
            view.render(model, request, response);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to render view" + e.getMessage());
        }
    }
}
