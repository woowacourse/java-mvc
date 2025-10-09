package com.interface21.webmvc.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HandlerDispatcher {

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public HandlerDispatcher(HandlerMappingRegistry handlerMappingRegistry,
                             HandlerAdapterRegistry handlerAdapterRegistry) {
        this.handlerMappingRegistry = handlerMappingRegistry;
        this.handlerAdapterRegistry = handlerAdapterRegistry;
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

    private ModelAndView dispatch(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerAdapter adapter = handlerAdapterRegistry.getHandlerAdapter(handler);
        return adapter.handle(request, response, handler);
    }

    private void render(ModelAndView mav, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (mav == null) {
            return;
        }

        final var view = mav.getView();
        if (view == null) {
            throw new IllegalStateException("렌더링할 뷰가 ModelAndView에 설정되어 있지 않습니다.");
        }
        view.render(mav.getModel(), request, response);
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
