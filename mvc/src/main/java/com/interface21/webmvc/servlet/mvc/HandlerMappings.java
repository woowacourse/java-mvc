package com.interface21.webmvc.servlet.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings;
    private final String basePackage;

    public HandlerMappings(String basePackage) {
        this.handlerMappings = new ArrayList<>();
        this.basePackage = basePackage;
    }

    public void initialize() {
        handlerMappings.add(new AnnotationHandlerMapping(basePackage));
        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
        }
    }

    public void add(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Optional<Object> findHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .filter(handlerMapping -> Objects.nonNull(handlerMapping.getHandler(request)))
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .findAny();
    }
}
