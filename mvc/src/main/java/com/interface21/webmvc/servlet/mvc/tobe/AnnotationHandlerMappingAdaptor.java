package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;

public class AnnotationHandlerMappingAdaptor implements HandlerMapping {

    private final AnnotationHandlerMapping annotationHandlerMapping;

    public AnnotationHandlerMappingAdaptor(AnnotationHandlerMapping annotationHandlerMapping) {
        this.annotationHandlerMapping = annotationHandlerMapping;
    }

    @Override
    public void initialize() {
        annotationHandlerMapping.initialize();
    }

    @Override
    public HandlerAdaptor getHandler(HttpServletRequest request) {
        Object handler = annotationHandlerMapping.getHandler(request);
        return new AnnotationHandlerAdaptor(handler);
    }
}
