package com.interface21.webmvc.servlet.mvc.handler.annotation;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.handler.HandlerAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutionHandlerAdapter implements HandlerAdapter {

    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }

    public ModelAndView handle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        return ((HandlerExecution) handler).handle(req, res);
    }
}
