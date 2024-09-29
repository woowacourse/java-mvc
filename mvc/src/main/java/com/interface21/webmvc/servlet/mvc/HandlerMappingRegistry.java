package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.annotation.AnnotationHandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappingRegistry {

    private static final String BASE_PACKAGE = "com.techcourse.controller";

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry() {
        this.handlerMappings = new ArrayList<>();
    }

    public void initialize() {
        handlerMappings.add(new AnnotationHandlerMapping(BASE_PACKAGE));

        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("처리할 수 없는 요청입니다."));
    }
}
