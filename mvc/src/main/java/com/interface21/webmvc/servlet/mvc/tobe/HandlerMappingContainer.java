package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HandlerMappingContainer {

    private static final String CONTROLLER_BASE_PACKAGE = "com.techcourse.controller";
    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingContainer() {
        this.handlerMappings = new ArrayList<>();
    }

    public void initialize() {
        addHandlerMapping(new AnnotationHandlerMapping(CONTROLLER_BASE_PACKAGE));
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Optional<Object> getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findAny();
    }
}
