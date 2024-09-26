package com.interface21.webmvc.servlet.mvc.tobe.mapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappingRegistry {

    private static final HandlerMappingRegistry INSTANCE = new HandlerMappingRegistry();

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    private HandlerMappingRegistry() {
    }

    public static HandlerMappingRegistry getInstance() {
        return INSTANCE;
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }
}
