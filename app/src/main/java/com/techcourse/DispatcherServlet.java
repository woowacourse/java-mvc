package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.mapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerExecutionHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.mapping.HandlerMappingRegistry;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @Override
    public void init() {
        handlerMappingRegistry.addHandlerMapping(new ManualHandlerMapping());
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("com.interface21.webmvc.controller"));
        handlerMappingRegistry.initialize();

        handlerAdapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            Object handler = handlerMappingRegistry.getHandler(req);
            HandlerAdapter adapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            ModelAndView modelAndView = adapter.handle(req, resp, handler);
            render(modelAndView, req, resp);
        } catch (Exception e) {
            throw new ServletException("DispatcherServlet service error", e);
        }
    }

    private void render(ModelAndView modelAndView, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        final View view = modelAndView.getView();
        final Map<String, Object> model = modelAndView.getModel();
        view.render(model, req, resp);
    }

}
