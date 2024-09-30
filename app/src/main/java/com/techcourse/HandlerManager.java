package com.techcourse;

import java.util.List;
import java.util.Objects;

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

    public Object getHandler(final HttpServletRequest request) throws ClassNotFoundException {
        return mappings.stream()
                .map(mapping -> mapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(getNotFoundController());
    }

    private Object getNotFoundController() throws ClassNotFoundException {
        return mappings.stream()
                .filter(ManualHandlerMapping.class::isInstance)
                .findFirst()
                .map(ManualHandlerMapping.class::cast)
                .map(ManualHandlerMapping::getNotFoundController)
                .orElseThrow(ClassNotFoundException::new);
    }
}
