package com.interface21.webmvc.servlet.mvc.tobe.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Optional<Object> getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.getHandler(request).isPresent())
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .findFirst()
                .orElse(Optional.empty());
    }
}
