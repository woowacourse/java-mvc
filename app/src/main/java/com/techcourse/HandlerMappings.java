package com.techcourse;

import com.techcourse.mapping.ManualHandlerMappingWrapper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;

public class HandlerMappings {

    private final List<HandlerMapping> mappings = new ArrayList<>();

    public void initialize() {
        final ManualHandlerMappingWrapper manualHandlerMapping = new ManualHandlerMappingWrapper();
        manualHandlerMapping.initialize();

        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping();
        annotationHandlerMapping.initialize();

        mappings.add(manualHandlerMapping);
        mappings.add(annotationHandlerMapping);
    }

    public Object getHandler(final HttpServletRequest request) {
        for (final HandlerMapping mapping : mappings) {
            final Object handler = mapping.getHandler(request);

            if (handler != null) {
                return handler;
            }
        }

        return null;
    }
}
