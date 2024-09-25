package com.techcourse.handler;

import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.techcourse.HandlerAdapter;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    private final AnnotationHandlerMapping handlerMapping;

    public AnnotationHandlerAdapter(String... basePackages) {
        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(basePackages);
        annotationHandlerMapping.initialize();
        this.handlerMapping = annotationHandlerMapping;
    }

    @Override
    public ModelAndView adapt(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final HandlerExecution handlerExecution = handlerMapping.getHandler(request);
        return handlerExecution.handle(request, response);
    }

    @Override
    public boolean support(final HttpServletRequest request) {
        final HandlerExecution handlerExecution = handlerMapping.getHandler(request);
        return !Objects.isNull(handlerExecution);
    }
}
