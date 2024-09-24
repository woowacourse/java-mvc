package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;

public class HandlerExecutionHandlerAdapter implements HandlerAdapter {

    private static final HandlerExecutionHandlerAdapter instance = new HandlerExecutionHandlerAdapter();

    private HandlerExecutionHandlerAdapter() {
    }

    public static HandlerExecutionHandlerAdapter getInstance() {
        return instance;
    }

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response) {
        HandlerExecution handlerExecution = (HandlerExecution) handler;
        return handlerExecution.handle(request, response);
    }
}

