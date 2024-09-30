package com.techcourse;

import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;

public class HandlerManager {

    private final List<HandlerMapping> mappings;

    public HandlerManager(HandlerMapping... handlerMappings) {
        this.mappings = List.of(handlerMappings);
    }

    public void initialize() {
        mappings.forEach(HandlerMapping::initialize);
    }

    public Object findHandler(final HttpServletRequest request) {
        final Optional<HandlerMapping> handlerMapping = mappings.stream()
                .filter(mapping -> mapping.hasHandler(request))
                .findFirst();

        if (handlerMapping.isEmpty()) {
            for (final HandlerMapping mapping : mappings) {
                if (mapping instanceof final ManualHandlerMapping manualHandlerMapping) {
                    return manualHandlerMapping.getNotFoundController();
                }
            }
        }
        return handlerMapping.get().getHandler(request);
    }
}
