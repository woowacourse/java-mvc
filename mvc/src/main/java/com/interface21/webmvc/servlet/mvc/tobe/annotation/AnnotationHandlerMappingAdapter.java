package com.interface21.webmvc.servlet.mvc.tobe.annotation;

import jakarta.servlet.http.HttpServletRequest;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingAdapter;

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
    //todo Optional??
    //지금은 예외가 터질거임
    public HandlerAdapter getHandler(HttpServletRequest request) {
        HandlerExecution handlerExecution = annotationHandlerMapping.getHandler(request);
        if (handlerExecution == null) {
            return null;
        }
        return new AnnotationHandlerAdapter(handlerExecution);
    }
}
