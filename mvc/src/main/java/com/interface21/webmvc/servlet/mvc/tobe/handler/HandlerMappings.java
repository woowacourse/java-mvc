package com.interface21.webmvc.servlet.mvc.tobe.handler;

import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings(final List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
        initHandlerMapping();
    }

    private void initHandlerMapping() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Object getHandler(final HttpServletRequest request) {
        final Optional<HandlerMapping> handlerMapping = handlerMappings.stream()
                .filter(mapping -> mapping.getHandler(request) != null)
                .findAny();

        return handlerMapping.map(mapping -> mapping.getHandler(request))
                .orElse(null);
    }
}
