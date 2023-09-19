package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public HandlerMappings addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
        return this;
    }

    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Optional<Object> getHandler(HttpServletRequest httpServletRequest) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            Object handler = handlerMapping.getHandler(httpServletRequest);
            if (handler != null) {
                return Optional.of(handler);
            }
        }
        return Optional.empty();
    }
}
