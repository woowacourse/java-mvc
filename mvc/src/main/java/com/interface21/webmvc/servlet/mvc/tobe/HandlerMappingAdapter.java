package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;

public class HandlerMappingAdapter implements HandlerMapping {

    private final AnnotationHandlerMapping annotationHandlerMapping;

    public HandlerMappingAdapter(AnnotationHandlerMapping annotationHandlerMapping) {
        this.annotationHandlerMapping = annotationHandlerMapping;
    }

    @Override
    public void initialize() {
        annotationHandlerMapping.initialize();
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return annotationHandlerMapping.getHandler(request);
    }
}
