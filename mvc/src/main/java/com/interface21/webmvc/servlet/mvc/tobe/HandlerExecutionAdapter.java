package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutionAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Class<?> handlerType) {
        return HandlerExecution.class.isAssignableFrom(handlerType);
    }

    @Override
    public ModelAndView handle(
            Object handler,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        HandlerExecution execution = (HandlerExecution) handler;
        return execution.handle(request, response);
    }
}
