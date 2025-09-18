package com.interface21.webmvc.servlet.mvc.handler.mapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public HandlerMappingRegistry() {
    }

    public void initialize() {
        final HandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping();
        addHandlerMapping(annotationHandlerMapping);
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        handlerMappings.add(handlerMapping);
    }

    public Object getHandler(final HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            final Object handler = handlerMapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        throw new RuntimeException("No handler found for request URI : " + request.getRequestURI());
    }
}
