package com.techcourse.support.web.mapping;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HandlerMappings {
    private final List<HandlerMapping> mappings;

    public HandlerMappings() {
        this.mappings = new ArrayList<>();
    }

    public void init() {
        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping();
        annotationHandlerMapping.initialize();
        final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        mappings.add(annotationHandlerMapping);
        mappings.add(manualHandlerMapping);
    }

    public Optional<Object> getHandler(final HttpServletRequest request) {
        for (final HandlerMapping handlerMapping : mappings) {
            final Object handler = handlerMapping.getHandler(request);
            if (Objects.nonNull(handler)) {
                return Optional.of(handler);
            }
        }
        return Optional.empty();
    }
}
