package com.techcourse;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet {

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;
    private final HandlerExecutor handlerExecutor;

    public DispatcherServlet() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerExecutor = new HandlerExecutor(handlerAdapterRegistry);
    }

    @Override
    public void init() {
        addHandlerMapping(new ManualHandlerMapping());
        addHandlerMapping(new AnnotationHandlerMapping("com.techcourse"));

        addHandlerAdapter(new ControllerHandlerAdapter());
        addHandlerAdapter(new HandlerExecutionHandlerAdapter());
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {

        try {
            Object handler = handlerMappingRegistry.getHandler(request)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "No handler found for " + request.getMethod() + " " + request.getRequestURI()
                    ));
            ModelAndView mav = handlerExecutor.handle(request, response, handler);
            render(mav, request, response);
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
    }

    protected void render(ModelAndView mav, HttpServletRequest request, HttpServletResponse response) throws Exception {
        mav.getView().render(mav.getModel(), request, response);
    }
}
