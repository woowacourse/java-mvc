package com.interface21.webmvc.servlet.mvc.handler.adapter;

import com.interface21.context.stereotype.Controller;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutionHandlerAdapter implements HandlerAdapter {

    public boolean supports(Object handler) {
        return handler.getClass().isAnnotationPresent(Controller.class);
    }

    public ModelAndView handle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        return ((HandlerExecution) handler).handle(req, res);
    }
}
