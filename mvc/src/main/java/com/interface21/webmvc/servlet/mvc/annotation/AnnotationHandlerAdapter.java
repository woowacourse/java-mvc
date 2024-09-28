package com.interface21.webmvc.servlet.mvc.annotation;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        final var handlerExecution = (HandlerExecution) handler;
        return handlerExecution.handle(request, response);
    }

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }
}
