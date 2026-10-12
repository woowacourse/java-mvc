package com.interface21.webmvc.servlet;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Objects;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry(final List<HandlerMapping> handlerMappings) {
        this.handlerMappings = List.copyOf(handlerMappings);
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }
}
