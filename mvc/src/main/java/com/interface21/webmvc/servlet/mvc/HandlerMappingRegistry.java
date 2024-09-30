package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public HandlerMapping getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.supports(request))
                .findFirst()
                .orElseThrow(() -> new HandlerNotFoundException(request.getRequestURI()));
    }

    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }
}
