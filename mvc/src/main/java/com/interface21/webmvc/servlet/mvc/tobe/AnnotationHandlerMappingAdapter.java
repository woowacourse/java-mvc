package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;

public class AnnotationHandlerMappingAdapter implements HandlerMapping {

    private final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(
            "com.interface21",
            "com.techcourse"
    );

    @Override
    public void initialize() {
        annotationHandlerMapping.initialize();
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return annotationHandlerMapping.getHandler(request);
    }
}
