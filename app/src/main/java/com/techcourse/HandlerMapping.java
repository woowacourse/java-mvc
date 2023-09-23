package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

public class HandlerMapping {

    private ManualHandlerMapping manualHandlerMapping;
    private AnnotationHandlerMapping annotationHandlerMapping;

    public void initialize() {
        manualHandlerMapping = new ManualHandlerMapping();
        annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");

        manualHandlerMapping.initialize();
        annotationHandlerMapping.initialize();
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        HandlerExecution handler = annotationHandlerMapping.getHandler(request);

        if (Objects.nonNull(handler)) {
            return handler;
        }

        handler = manualHandlerMapping.getHandler(request);
        return handler;
    }

}
