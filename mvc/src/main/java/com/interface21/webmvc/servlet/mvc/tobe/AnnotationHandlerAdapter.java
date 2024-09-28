package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean canHandle(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public void handle(Object handler, HttpServletRequest request, HttpServletResponse response) {
        try {
            ModelAndView modelAndView = ((HandlerExecution) handler).handle(request, response);
            modelAndView.render(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
