package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    private final AnnotationHandlerMapping handlerMapping;

    public AnnotationHandlerAdapter(AnnotationHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @Override
    public boolean isSupports(HttpServletRequest request) {
        Object handler = handlerMapping.getHandler(request);
        return handler != null;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        HandlerExecution handler = (HandlerExecution) handlerMapping.getHandler(request);
        return handler.handle(request, response);
    }
}
