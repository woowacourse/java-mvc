package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry() {
        this.handlerMappings = new ArrayList<>();
    }

    public HandlerMappingRegistry(final List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Optional<Object> getHandler(final HttpServletRequest request) {
        if (request == null) {
            return Optional.empty();
        }

        for (final HandlerMapping handlerMapping : handlerMappings) {
            final Object handler = handlerMapping.getHandler(request);
            if (handler != null) {
                return Optional.of(handler);
            }
        }

        return Optional.empty();
    }
}
