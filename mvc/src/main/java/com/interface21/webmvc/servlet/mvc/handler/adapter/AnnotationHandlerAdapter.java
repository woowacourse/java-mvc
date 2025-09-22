package com.interface21.webmvc.servlet.mvc.handler.adapter;

import com.interface21.webmvc.servlet.mvc.handler.mapping.HandlerExecution;
import com.interface21.webmvc.servlet.view.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean isHandlingPossible(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handler(Object handler, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return ((HandlerExecution) handler).handle(request, response);
    }
}
