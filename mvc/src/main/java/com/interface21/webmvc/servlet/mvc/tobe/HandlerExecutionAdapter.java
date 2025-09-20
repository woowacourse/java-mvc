package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutionAdapter implements HandlerAdapter{
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            ModelAndView modelAndView = ((HandlerExecution)handler).handle(request, response);
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean support(Object handler) {
        return handler instanceof HandlerExecution;
    }
}
