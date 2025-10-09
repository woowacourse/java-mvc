package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet {

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;
    private final HandlerDispatcher handlerDispatcher;

    public DispatcherServlet() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerDispatcher = new HandlerDispatcher(handlerMappingRegistry, handlerAdapterRegistry);
    }

    @Override
    public void init() {
        addHandlerMapping(new AnnotationHandlerMapping("com.techcourse"));

        addHandlerAdapter(new AnnotationHandlerAdapter());
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        if (handlerMapping instanceof InitializableHandlerMapping) {
            ((InitializableHandlerMapping) handlerMapping).initialize();
        }
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        handlerDispatcher.execute(request, response);
    }
}
