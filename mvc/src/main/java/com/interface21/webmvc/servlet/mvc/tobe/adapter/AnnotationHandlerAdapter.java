package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    private HandlerExecution handlerExecution;

    @Override
    public boolean supports(Object handler) {
        if (handler instanceof HandlerExecution) {
            this.handlerExecution = (HandlerExecution) handler;
            return true;
        }
        return false;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
        return handlerExecution.handle(request, response);
    }
}
