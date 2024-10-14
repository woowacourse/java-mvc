package com.interface21.webmvc.servlet.mvc.annotation;

import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.HandlerMappingAdapter;
import jakarta.servlet.http.HttpServletRequest;

public class AnnotationHandlerMappingAdapter implements HandlerMappingAdapter {
    private final AnnotationHandlerMapping annotationHandlerMapping;

    public AnnotationHandlerMappingAdapter(Object... basePackage) {

        this.annotationHandlerMapping = new AnnotationHandlerMapping(basePackage);
    }

    @Override
    public void initialize() {
        annotationHandlerMapping.initialize();
    }

    @Override
    public HandlerAdapter getHandler(HttpServletRequest request) {
        HandlerExecution handlerExecution = annotationHandlerMapping.getHandler(request);
        if (handlerExecution == null) {
            return null;
        }
        return new AnnotationHandlerAdapter(handlerExecution);
    }
}
