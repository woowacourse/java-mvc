package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.handler.adapter.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.handler.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.handler.adapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.handler.adapter.HandlerExecutionHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.handler.mapping.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.handler.mapping.AnnotationHandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappingRegistry handlerMappingRegistry;
    private HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet(HandlerMappingRegistry handlerMappingRegistry, HandlerAdapterRegistry handlerAdapterRegistry) {
        this.handlerMappingRegistry = handlerMappingRegistry;
        this.handlerAdapterRegistry = handlerAdapterRegistry;
    }

    @Override
    public void init() {
        handlerMappingRegistry.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        try {
            Object handler = getHandler(request);
            HandlerAdapter handlerAdapter = getAdapter(handler);
            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(HttpServletRequest request) {
        return handlerMappingRegistry.getHandler(request);
    }

    private HandlerAdapter getAdapter(Object handler) {
        return handlerAdapterRegistry.getHandlerAdapter(handler);
    }

}
