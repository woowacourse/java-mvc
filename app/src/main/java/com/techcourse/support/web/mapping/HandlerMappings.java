package com.techcourse.support.web.mapping;

import com.techcourse.ManualHandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappings {
    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings() {
        this.handlerMappings = new ArrayList<>();
    }

    public void init() {
        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping();
        annotationHandlerMapping.initialize();
        final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        
        handlerMappings.add(annotationHandlerMapping);
        handlerMappings.add(manualHandlerMapping);
    }

    public Object getHandler(final HttpServletRequest request) {
        for (final HandlerMapping handlerMapping : handlerMappings) {
            final Object handler = handlerMapping.getHandler(request);
            if (Objects.nonNull(handler)) {
                return handler;
            }
        }
        return null;
    }
}
