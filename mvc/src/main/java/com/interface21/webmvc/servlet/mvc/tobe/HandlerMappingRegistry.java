package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry(final List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    public static HandlerMappingRegistry empty() {
        return new HandlerMappingRegistry(new ArrayList<>());
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        if (handlerMapping != null) {
            handlerMappings.add(handlerMapping);
        }
    }

    public Optional<Object> getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
            .map(handlerMapping -> handlerMapping.getHandler(request))
            .filter(Objects::nonNull)
            .findFirst();
    }
}
