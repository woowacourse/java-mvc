package com.techcourse.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.techcourse.HandlerMappingAdapter;

public class AnnotationHandlerMappingAdapter implements HandlerMappingAdapter {

    private final AnnotationHandlerMapping handlerMapping;

    public AnnotationHandlerMappingAdapter(String... basePackages) {
        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(basePackages);
        annotationHandlerMapping.initialize();
        this.handlerMapping = annotationHandlerMapping;
    }

    @Override
    public ModelAndView adapt(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final HandlerExecution handlerExecution = handlerMapping.getHandler(request);
        return handlerExecution.handle(request, response);
    }
}
