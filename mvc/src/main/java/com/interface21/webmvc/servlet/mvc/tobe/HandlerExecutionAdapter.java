package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutionAdapter implements HandlerAdapter {

    @Override
    public ModelAndView invoke(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerExecution handlerExecution = (HandlerExecution) handler;

        return handlerExecution.handle(request, response);
    }
}
