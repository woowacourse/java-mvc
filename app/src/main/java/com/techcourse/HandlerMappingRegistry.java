package com.techcourse;

import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    void initialize() {
        final String basePackage = Application.class.getPackage().getName();
        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(basePackage);
        annotationHandlerMapping.initialize();
        addHandlerMapping(annotationHandlerMapping);
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        this.handlerMappings.add(handlerMapping);
    }

    public Optional<Object> getHandler(final HttpServletRequest request) {
        for (final HandlerMapping handlerMapping : handlerMappings) {
            final Object handler = handlerMapping.getHandler(request);
            if (handler != null) {
                return Optional.of(handler);
            }
        }
        return Optional.empty();
    }
}
