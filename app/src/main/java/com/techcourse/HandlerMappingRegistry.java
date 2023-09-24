package com.techcourse;

import webmvc.org.springframework.web.servlet.mvc.exception.HandlerMappingNotFoundException;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.Request;

import java.util.ArrayList;
import java.util.List;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry() {
        handlerMappings = new ArrayList<>();
    }

    public void initialize(final String packageName) {
        handlerMappings.add(new AnnotationHandlerMapping(packageName));
        handlerMappings.add(new ManualHandlerMapping());
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Object getHandler(final Request request) {
        return handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.hasMapping(request))
                .findFirst()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .orElseThrow(HandlerMappingNotFoundException::new);
    }
}
