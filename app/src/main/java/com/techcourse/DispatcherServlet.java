package com.techcourse;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.InitializableHandlerMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.MethodHandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DispatcherServlet extends HttpServlet {

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;
    private final HandlerDispatcher handlerDispatcher;

    public DispatcherServlet() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerDispatcher = new HandlerDispatcher(handlerAdapterRegistry);
    }

    @Override
    public void init() {
        addHandlerMapping(new ManualHandlerMapping());
        addHandlerMapping(new AnnotationHandlerMapping("com.techcourse"));

        addHandlerAdapter(new ControllerHandlerAdapter());
        addHandlerAdapter(new MethodHandlerAdapter());
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
        try {
            Object handler = findHandler(request);
            ModelAndView mav = handlerDispatcher.dispatch(request, response, handler);
            render(mav, request, response);
        } catch (IllegalArgumentException e) {
            handleNotFound(response, e);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private Object findHandler(final HttpServletRequest request) {
        return handlerMappingRegistry.getHandler(request)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No handler found for " + request.getMethod() + " " + request.getRequestURI()
                ));
    }

    private void handleNotFound(final HttpServletResponse response, final IllegalArgumentException e) 
            throws ServletException {
        try {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (IOException ex) {
            throw new ServletException(ex);
        }
    }

    private void handleException(final Exception e) throws ServletException {
        throw new ServletException(e.getMessage());
    }

    protected void render(ModelAndView mav, HttpServletRequest request, HttpServletResponse response) throws Exception {
        mav.getView().render(mav.getModel(), request, response);
    }
}