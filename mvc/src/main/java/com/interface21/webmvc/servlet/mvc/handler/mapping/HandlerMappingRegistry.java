package com.interface21.webmvc.servlet.mvc.handler.mapping;

import com.interface21.webmvc.servlet.NoHandlerFoundException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    private HandlerMappingRegistry(List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    public static HandlerMappingRegistry empty() {
        return new HandlerMappingRegistry(new ArrayList<>());
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(mapping -> mapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new NoHandlerFoundException(request.getMethod(), request.getRequestURI()));
    }

    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

}
