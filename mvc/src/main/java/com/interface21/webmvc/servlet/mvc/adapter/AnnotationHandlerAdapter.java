package com.interface21.webmvc.servlet.mvc.adapter;

import com.interface21.webmvc.servlet.mvc.handler.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.view.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof HandlerExecution);
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerExecution handlerExecution = (HandlerExecution) handler;

        return handlerExecution.handle(request, response);
    }
}
