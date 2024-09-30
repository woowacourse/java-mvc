package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

public class RequestHandlerAdapter implements HandlerAdapter {

    private final List<HandlerMapping> handlerMappings;

    public RequestHandlerAdapter(List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, ReflectiveOperationException {
        Optional<RequestHandler> handlerOptional = getHandler(request);
        if (handlerOptional.isEmpty()) {
            throw new ServletException("No handler found for request URI: " + request.getRequestURI());
        }
        return handlerOptional.get().handle(request, response);
    }

    private Optional<RequestHandler> getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        return handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.canHandle(method, requestURI))
                .findFirst()
                .map(handlerMapping -> handlerMapping.getHandler(method, requestURI));
    }
}
