package com.interface21.webmvc.servlet.mvc;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry(final Object... basePackage) {
        this.handlerMappings = new ArrayList<>();
        initialize(basePackage);
    }

    private void initialize(final Object... basePackage) {
        handlerMappings.add(new AnnotationHandlerMapping(basePackage));
    }

    public void add(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public HandlerMapping getHandlerMapping(final HttpServletRequest request) {
        return handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.hasHandler(request))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 요청을 처리할 수 있는 핸들러가 없습니다."));
    }
}
