package com.techcourse.servlet.adaptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

public class AnnotationHandlerAdaptor implements HandlerAdaptor {
    @Override
    public boolean supports(final Object handler) {
        return handler.getClass().isAnnotationPresent(RequestMapping.class);
    }

    @Override
    public ModelAndView handle(final Object controller, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final HandlerExecution handlerExecution = (HandlerExecution) controller;
        return handlerExecution.handle(request, response);
    }
}
