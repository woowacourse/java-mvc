package com.interface21.webmvc.servlet.mvc.tobe.annotation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;

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
