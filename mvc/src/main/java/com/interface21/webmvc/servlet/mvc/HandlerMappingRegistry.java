package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.mvc.mapping.AnnotationHandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HandlerMappingRegistry {

    private final String controllerScanBase;
    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry(String basePackage) {
        this.controllerScanBase = Objects.requireNonNull(basePackage);
        handlerMappings = new ArrayList<>();
    }

    public void initialize() {
        handlerMappings.add(new AnnotationHandlerMapping(controllerScanBase));
    }

    public void addHandlerMapping(HandlerMapping mapping) {
        handlerMappings.add(mapping);
    }

    public Optional<Object> getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst();
    }
}
