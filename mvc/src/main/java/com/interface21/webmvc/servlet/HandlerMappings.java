package com.interface21.webmvc.servlet;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import com.interface21.webmvc.servlet.mvc.asis.ControllerHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import jakarta.servlet.http.HttpServletRequest;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public HandlerMappings(String basePackage) {
        handlerMappings.add(new AnnotationHandlerMapping(basePackage));
        handlerMappings.add(new ControllerHandlerMapping(basePackage));

        initialize();
    }

    private void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(mapping -> mapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("핸들러가 존재하지 않습니다. " + request.getRequestURI()));
    }

    public void appendHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }
}
