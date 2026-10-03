package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutionAdapter implements HandlerAdapter {

    @Override
    public boolean isSupported(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView adapt(HttpServletRequest request, HttpServletResponse response,
        Object handler) {
        return ((HandlerExecution) handler).handle(request, response);
    }
}
