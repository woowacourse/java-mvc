package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;

public class RequestHandlerMapping {

    private final AnnotationHandlerMapping annotationHandlerMapping;

    public RequestHandlerMapping() {
        this.annotationHandlerMapping = new AnnotationHandlerMapping("com");
        initHandlerMapping();
    }

    private void initHandlerMapping() {
        annotationHandlerMapping.initialize();
    }

    public Object getHandler(final HttpServletRequest request) {
        return annotationHandlerMapping.findHandler(request).orElse(null);
    }
}
