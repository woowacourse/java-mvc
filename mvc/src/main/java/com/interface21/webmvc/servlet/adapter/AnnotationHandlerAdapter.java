package com.interface21.webmvc.servlet.adapter;

import com.interface21.webmvc.servlet.view.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean support(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        return ((HandlerExecution) handler).handle(req, resp);
    }
}
