package com.interface21.webmvc.servlet.mvc.handler.mapping;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    public Optional<Object> getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(mapping -> mapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst();
    }

}
