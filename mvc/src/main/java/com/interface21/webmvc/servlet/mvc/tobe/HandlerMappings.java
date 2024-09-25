package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings(List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
        this.handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Object mapToHandler(HttpServletRequest request) throws ServletException {
        for (HandlerMapping handlerMapping : handlerMappings) {
            Object handler = handlerMapping.findHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        throw new ServletException("No handler found for requestURI: " + request.getRequestURI());
    }
}
