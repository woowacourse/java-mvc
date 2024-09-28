package com.interface21.webmvc.servlet.mvc.tobe.mapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public HandlerMappingRegistry() {
    }

    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Optional<Object> getHandler(HttpServletRequest httpServletRequest) {
        return handlerMappings.stream()
                .filter(hm -> hm.getHandler(httpServletRequest) != null)
                .map(hm -> hm.getHandler(httpServletRequest))
                .findFirst();
    }
}
