package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry(final List<HandlerMapping> handlerMappings) {
        handlerMappings.forEach(HandlerMapping::initialize);
        this.handlerMappings = handlerMappings;
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new NoHandlerFoundException(request.getMethod(), request.getRequestURI()));
    }
}
