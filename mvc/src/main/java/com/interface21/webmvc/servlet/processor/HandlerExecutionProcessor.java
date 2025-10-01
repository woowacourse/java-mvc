package com.interface21.webmvc.servlet.processor;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutionProcessor implements HandlerProcessor {

    @Override
    public boolean supports(Object handler) {
        return HandlerExecution.class.isAssignableFrom(handler.getClass());
    }

    @Override
    public ModelAndView process(
            final Object handler,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
        try {
            HandlerExecution handlerExecution = (HandlerExecution) handler;
            return handlerExecution.handle(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
