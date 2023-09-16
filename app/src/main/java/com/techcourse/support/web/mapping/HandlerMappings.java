package com.techcourse.support.web.mapping;

import com.techcourse.ManualHandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappings {
    private final List<HandlerMapping> mappings;

    public HandlerMappings() {
        this.mappings = new ArrayList<>();
    }

    public void init() {
        final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping();
        annotationHandlerMapping.initialize();

        mappings.add(manualHandlerMapping);
        mappings.add(annotationHandlerMapping);
    }

    public Object getHandler(final HttpServletRequest request) {
        for (final HandlerMapping handlerMapping : mappings) {
            final Object handler = handlerMapping.getHandler(request);
            if (Objects.nonNull(handler)) {
                return handler;
            }
        }
        return null;
    }
}
