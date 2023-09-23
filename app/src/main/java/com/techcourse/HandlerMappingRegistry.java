package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.asis.HandlerMapping;

import java.util.List;

public class HandlerMappingRegistry {


    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry(final List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
        initialize(handlerMappings);
    }

    private void initialize(final List<HandlerMapping> handlerMappings) {
        for (final HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
        }
    }

    public HandlerMapping getHandlerMapping(final HttpServletRequest request) {
        return handlerMappings.stream()
                              .filter(handlerMapping -> handlerMapping.containsHandler(request))
                              .findAny()
                              .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 handler입니다."));
    }
}
