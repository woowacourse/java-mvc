package com.interface21.webmvc.servlet.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import com.interface21.webmvc.servlet.HandlerMapping;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry(final Object... basePackage) {
        this.handlerMappings = new ArrayList<>();
        initialize(basePackage);
    }

    private void initialize(final Object... basePackage) {
        handlerMappings.add(new AnnotationHandlerMapping(basePackage));
    }

    public void add(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Optional<Object> getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.hasHandler(request))
                .findFirst()
                .map(handlerMapping -> handlerMapping.getHandler(request));
    }
}
