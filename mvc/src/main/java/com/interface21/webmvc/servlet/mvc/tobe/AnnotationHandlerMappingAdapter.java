package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;

public class AnnotationHandlerMappingAdapter implements HandlerMapping {

    private final AnnotationHandlerMapping annotationHandlerMapping;

    public AnnotationHandlerMappingAdapter(AnnotationHandlerMapping annotationHandlerMapping) {
        this.annotationHandlerMapping = annotationHandlerMapping;
    }

    @Override
    public void initialize() {
        annotationHandlerMapping.initialize();
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        try {
            return annotationHandlerMapping.getHandler(request);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
