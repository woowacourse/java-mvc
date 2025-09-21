package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutionAdapter implements HandlerAdapter{
    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            return ((HandlerExecution)handler).handle(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean support(Object handler) {
        return handler instanceof HandlerExecution;
    }
}
