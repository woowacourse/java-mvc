package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Optional<Object> getHandler(final HttpServletRequest request) {
        for (final HandlerMapping handlerMapping : handlerMappings) {
            if (handlerMapping.getHandler(request) != null) {
                return Optional.ofNullable(handlerMapping.getHandler(request));
            }
        }
        return Optional.empty();
    }
}
