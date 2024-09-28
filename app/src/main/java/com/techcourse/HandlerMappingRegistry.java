package com.techcourse;

import java.util.List;
import java.util.stream.Stream;

import jakarta.servlet.http.HttpServletRequest;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry(final Object... basePackages) {
        handlerMappings = List.of(new ManualHandlerMapping(), new AnnotationHandlerMapping(basePackages));
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .flatMap(handlerMapping -> Stream.ofNullable(handlerMapping.getHandler(request)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No handler found for request URI: " + request.getRequestURI()));
    }
}
