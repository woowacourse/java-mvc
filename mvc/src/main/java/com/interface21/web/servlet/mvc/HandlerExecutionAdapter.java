package com.interface21.web.servlet.mvc;

import com.interface21.web.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutionAdapter implements HandlerAdapter {

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerExecution HandlerExecution = (HandlerExecution) handler;
        return HandlerExecution.handle(request, response);
    }

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }
}
