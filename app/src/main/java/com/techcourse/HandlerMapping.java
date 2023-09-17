package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;

public class HandlerMapping {

    private final ManualHandlerMapping manualHandlerMapping;
    private final AnnotationHandlerMapping annotationHandlerMapping;
    private final DefaultHandlerMapping defaultHandlerMapping;

    public HandlerMapping(final String packageName) {
        this.manualHandlerMapping = new ManualHandlerMapping();
        this.annotationHandlerMapping = new AnnotationHandlerMapping(packageName);
        this.defaultHandlerMapping = new DefaultHandlerMapping();
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

        return defaultHandlerMapping.getNotFoundController();
    }
}
