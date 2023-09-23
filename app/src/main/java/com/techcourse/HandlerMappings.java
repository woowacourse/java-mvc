package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;

public class HandlerMappings {

    private static final String NOT_FOUND_URI = "/404";

    private final ManualHandlerMapping manualHandlerMapping;
    private final AnnotationHandlerMapping annotationHandlerMapping;

    public HandlerMappings(final String packageName) {
        this.manualHandlerMapping = new ManualHandlerMapping();
        this.annotationHandlerMapping = new AnnotationHandlerMapping(packageName);
    }

    public void initialize() {
        manualHandlerMapping.initialize();
        annotationHandlerMapping.initialize();
    }

    public Object getHandler(final HttpServletRequest request) {
        final Controller manualHandler = manualHandlerMapping.getHandler(request.getRequestURI());
        if (Objects.nonNull(manualHandler)) {
            return manualHandler;
        }

        final Object annotationHandler = annotationHandlerMapping.getHandler(request);
        if (Objects.nonNull(annotationHandler)) {
            return annotationHandler;
        }

        return manualHandlerMapping.getHandler(NOT_FOUND_URI);
    }
}
