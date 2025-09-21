package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
        handlerMappings = new ArrayList<>();
        handlerAdapters = new ArrayList<>();
    }

    @Override
    public void init() {
        handlerMappings.add(new ManualHandlerMapping());
        handlerMappings.add(new AnnotationHandlerMapping("com.interface21.webmvc.controller"));
        handlerMappings.forEach(HandlerMapping::initialize);

        handlerAdapters.add(new ControllerHandlerAdapter());
        handlerAdapters.add(new HandlerExecutionHandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            Object handler = getHandler(req);
            HandlerAdapter adapter = getHandlerAdapter(handler);
            ModelAndView modelAndView = adapter.handle(req, resp, handler);
            modelAndView.render(req, resp);
        } catch (Exception e) {
            throw new ServletException("DispatcherServlet service error", e);
        }
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        for (HandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new IllegalArgumentException("No adapter found for handler: " + handler);
    }

    private Object getHandler(HttpServletRequest req) {
        for (HandlerMapping mapping : handlerMappings) {
            Object handler = mapping.getHandler(req);
            if (handler != null) {
                return handler;
            }
        }
        return null;
    }
}
