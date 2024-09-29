package com.interface21.webmvc.servlet.mvc.handler;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdaptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutionAdaptor implements HandlerAdaptor {

    @Override
    public boolean canExecute(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView execute(Object handler, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return ((HandlerExecution) handler).handle(request, response);
    }
}
