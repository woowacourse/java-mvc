package com.techcourse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.HandlerMapping;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void add(HandlerMapping handlerMapping) {
        this.handlerMappings.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

}
