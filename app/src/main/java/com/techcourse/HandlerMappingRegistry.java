package com.techcourse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.interface21.webmvc.servlet.mvc.HandlerMapping;

import jakarta.servlet.http.HttpServletRequest;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
        handlerMapping.initialize();
    }

    Optional<Object> getHandler(final HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            Object handler = handlerMapping.getHandler(request);
            if (handler != null) {
                return Optional.of(handler);
            }
        }
        return Optional.empty();
    }
}
