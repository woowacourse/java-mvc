package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import jakarta.servlet.http.HttpServletRequest;

import com.interface21.context.container.Container;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry() {
        Container container = Container.getInstance();
        handlerMappings = container.getInstancesOf(HandlerMapping.class);
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No handler found for request URI: " + request.getRequestURI()));
    }
}
