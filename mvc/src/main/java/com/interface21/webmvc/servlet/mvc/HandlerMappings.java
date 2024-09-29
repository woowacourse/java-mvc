package com.interface21.webmvc.servlet.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;

public class HandlerMappings {

    private static final String BASE_PACKAGE = "com.techcourse.controller";

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings() {
        this.handlerMappings = new ArrayList<>();
    }

    public void initialize() {
        handlerMappings.add(new AnnotationHandlerMapping(BASE_PACKAGE));
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
