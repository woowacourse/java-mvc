package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.view.JsonView;
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

        addHandlerView("json", new JsonView());
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

    public void addHandlerView(final String viewName, final View view) {
        handlerDispatcher.addView(viewName, view);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        handlerDispatcher.execute(request, response);
    }
}
