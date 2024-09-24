package com.interface21.webmvc.servlet.mvc.tobe.mapping;

import java.util.Objects;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;

public class HandlerMappings {

    private final Set<HandlerMapping> handlerMappings;

    public HandlerMappings(HandlerMapping... handlerMappings) {
        this.handlerMappings = Set.of(handlerMappings);
    }

    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
