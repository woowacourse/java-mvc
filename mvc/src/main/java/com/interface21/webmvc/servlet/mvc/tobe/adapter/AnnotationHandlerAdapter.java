package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.exception.UnprocessableHandlerException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdapter implements HandlerAdapter{

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HandlerExecution handlerExecution = convertHandler(handler);
        return handlerExecution.handle(request, response);
    }

    private HandlerExecution convertHandler(Object handler) {
        if (!supports(handler)) {
            throw new UnprocessableHandlerException(handler.getClass().getName());
        }

        return (HandlerExecution) handler;
    }
}
