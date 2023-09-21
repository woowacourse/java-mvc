package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

public class HandlerExecutionAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof HandlerExecution);
    }

    @Override
    public String adapt(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ModelAndView handle = ((HandlerExecution) handler).handle(request, response);
        return (String) handle.getView();
    }
}
