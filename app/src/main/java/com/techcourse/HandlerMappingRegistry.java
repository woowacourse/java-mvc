package com.techcourse;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry() {
        this.handlerMappings = new ArrayList<>();

        final var manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        adHandlerMapping(manualHandlerMapping);

        final var annotationHandlerMapping = new AnnotationHandlerMapping();
        annotationHandlerMapping.initialize();
        adHandlerMapping(annotationHandlerMapping);
    }

    public Object getHandler(final HttpServletRequest request) throws ServletException {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(
                        () -> new ServletException("No handler found for request URI : " + request.getRequestURI())
                );
    }

    private void adHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }
}
