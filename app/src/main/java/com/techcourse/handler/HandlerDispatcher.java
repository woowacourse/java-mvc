package com.techcourse.handler;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HandlerDispatcher {

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;
    private final Map<String, View> views = new HashMap<>();

    public HandlerDispatcher(HandlerMappingRegistry handlerMappingRegistry,
                             HandlerAdapterRegistry handlerAdapterRegistry) {
        this.handlerMappingRegistry = handlerMappingRegistry;
        this.handlerAdapterRegistry = handlerAdapterRegistry;
    }

    public void addView(final String viewName, final View view) {
        views.put(viewName, view);
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            Object handler = findHandler(request);
            ModelAndView mav = dispatch(request, response, handler);
            render(mav, request, response);
        } catch (IllegalArgumentException e) {
            handleNotFound(response, e);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private Object findHandler(HttpServletRequest request) {
        return handlerMappingRegistry.getHandler(request)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No handler found for " + request.getMethod() + " " + request.getRequestURI()
                ));
    }

    private ModelAndView dispatch(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HandlerAdapter adapter = handlerAdapterRegistry.getHandlerAdapter(handler);
        return adapter.handle(request, response, handler);
    }

    private void render(ModelAndView mav, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (mav == null) {
            return;
        }

        if (mav.isReference()) {
            final var viewName = mav.getViewName();
            if (views.containsKey(viewName)) {
                views.get(viewName).render(mav.getModel(), request, response);
                return;
            }
            request.getRequestDispatcher(viewName).forward(request, response);
            return;
        }
        mav.getView().render(mav.getModel(), request, response);
    }

    private void handleNotFound(HttpServletResponse response, IllegalArgumentException e) throws ServletException {
        try {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (IOException ex) {
            throw new ServletException(ex);
        }
    }

    private void handleException(Exception e) throws ServletException {
        throw new ServletException(e.getMessage());
    }
}
