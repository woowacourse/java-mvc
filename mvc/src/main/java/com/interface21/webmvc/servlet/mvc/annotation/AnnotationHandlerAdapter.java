package com.interface21.webmvc.servlet.mvc.annotation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerExecution;

public class AnnotationHandlerAdapter implements HandlerAdapter {
    private final HandlerExecution handlerExecution;

    public AnnotationHandlerAdapter(HandlerExecution handlerExecution) {
        this.handlerExecution = handlerExecution;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return handlerExecution.handle(request, response);
    }
}
