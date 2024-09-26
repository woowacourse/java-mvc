package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry(List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(
                        () -> new IllegalArgumentException("No suitable handler found for " + request.getRequestURI()));
    }

    @Override
    public String toString() {
        return "HandlerMappingRegistry{" +
                "handlerMappings=" + handlerMappings +
                '}';
    }
}
