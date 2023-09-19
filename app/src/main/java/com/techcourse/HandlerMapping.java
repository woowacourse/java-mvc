package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import webmvc.org.springframework.web.servlet.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;

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
        HandlerExecution handler = manualHandlerMapping.getHandler(request.getRequestURI());

        if (Objects.nonNull(handler)) {
            return handler;
        }

        handler = annotationHandlerMapping.getHandler(request);
        validateRequest(handler);

        return handler;
    }

    private void validateRequest(final HandlerExecution handler) {
        if (Objects.isNull(handler)) {
            throw new IllegalArgumentException("요청에 맞는 Controller 를 찾지 못했습니다.");
        }
    }

}
