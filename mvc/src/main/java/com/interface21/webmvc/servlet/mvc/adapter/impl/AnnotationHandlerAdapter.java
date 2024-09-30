package com.interface21.webmvc.servlet.mvc.adapter.impl;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.mapping.impl.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.mapping.impl.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof AnnotationHandlerMapping;
    }

    @Override
    public ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HandlerExecution handlerExecution = (HandlerExecution) ((AnnotationHandlerMapping) handler).getHandler(request);
        return handlerExecution.handle(request, response);
    }
}
