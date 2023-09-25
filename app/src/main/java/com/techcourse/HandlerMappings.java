package com.techcourse;

import com.techcourse.exception.NoSuchHandlerFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;

public class HandlerMappings {

    private final ManualHandlerMapping manualHandlerMapping;
    private final AnnotationHandlerMapping annotationHandlerMapping;

    public HandlerMappings() {
        this.manualHandlerMapping = new ManualHandlerMapping();
        this.annotationHandlerMapping = new AnnotationHandlerMapping();
        manualHandlerMapping.initialize();
        annotationHandlerMapping.initialize();
    }

    public Object getHandler(HttpServletRequest request) {
        String url = request.getRequestURI();
        Object manualHandler = manualHandlerMapping.getHandler(url);
        if (Objects.nonNull(manualHandler)) {
            return manualHandler;
        }
        Object annotationHandler = annotationHandlerMapping.getHandler(request);
        if (Objects.nonNull(annotationHandler)) {
            return annotationHandler;
        }
        throw new NoSuchHandlerFoundException();
    }
}
