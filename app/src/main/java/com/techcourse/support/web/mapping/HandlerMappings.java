package com.techcourse.support.web.mapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;

public class HandlerMappings {

    private final List<HandlerMapping> mapping = new ArrayList<>();

    public void initialize() {
        final ManualHandlerMappingWrapper manualHandlerMapping = new ManualHandlerMappingWrapper();
        manualHandlerMapping.initialize();

        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping();
        annotationHandlerMapping.initialize();

        mapping.add(manualHandlerMapping);
        mapping.add(annotationHandlerMapping);
    }

    public Object getHandler(final HttpServletRequest request) {
        for (final HandlerMapping targetHandlerMapping : mapping) {
            final Object handler = targetHandlerMapping.getHandler(request);

            if (handler != null) {
                return handler;
            }
        }

        return null;
    }
}
