package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

public class HandlerMappingRegistry {

    private final Set<HandlerMapping> handlerMappings = new HashSet<>();

    public HandlerMappingRegistry() {
        handlerMappings.add(new ManualHandlerMapping());
        handlerMappings.add(new AnnotationHandlerMapping("com"));
    }

    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Optional<Object> getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(it -> it.getHandler(request))
                .filter(Optional::isPresent)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("요청 URI을 처리할 핸들러가 없습니다. 요청 URI=" + request.getRequestURI()));
    }
}
