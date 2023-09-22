package com.techcourse.support.web.adaptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdaptor;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

public class AnnotationHandlerAdaptor implements HandlerAdaptor {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HandlerExecution handlerExecution = (HandlerExecution) handler;

        return handlerExecution.handle(request, response);
    }
}
