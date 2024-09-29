package com.interface21.webmvc.servlet.mvc.tobe.registry;

import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;

import java.util.*;

public class HandlerMappingRegistry {

    private static final List<HandlerMapping> handlerMappings = new ArrayList<>();

    private HandlerMappingRegistry() {
    }

    public static void addHandlerMappings(Set<HandlerMapping> initializedHandlerMappings) {
        initializedHandlerMappings.stream()
                .filter(HandlerMappingRegistry::isNotContainedHandler)
                .forEach(handlerMappings::add);
    }

    private static boolean isNotContainedHandler(HandlerMapping handlerMapping) {
        return !handlerMappings.contains(handlerMapping);
    }

    public static Optional<Object> getHandler(HttpServletRequest httpServletRequest) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(httpServletRequest))
                .filter(Objects::nonNull)
                .findAny();
    }
}
