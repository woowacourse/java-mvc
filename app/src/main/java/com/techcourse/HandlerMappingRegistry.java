package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public HandlerMappingRegistry() {
        handlerMappings.add(new ManualHandlerMapping());
        handlerMappings.add(new AnnotationHandlerMapping("com"));
    }

    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        if (handlerMappings.contains(handlerMapping)) {
            throw new RuntimeException("이미 존재합니다.");
        }
        handlerMappings.add(handlerMapping);
    }

    public Optional<Object> getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(it -> it.getHandler(request))
                .filter(Optional::isPresent)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("요청 URI을 처리할 핸들러가 없습니다. 요청 URI=" + request.getRequestURI()));
    }
}
