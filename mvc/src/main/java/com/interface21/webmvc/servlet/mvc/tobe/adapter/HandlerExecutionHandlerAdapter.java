package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.handler.HandlerExecution;

public class HandlerExecutionHandlerAdapter implements HandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(HandlerExecutionHandlerAdapter.class);

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) throws Exception {
        log.info("[Adapter 실행] - HandlerExecutionHandlerAdapter");

        HandlerExecution handlerExecution = (HandlerExecution) handler;
        return handlerExecution.handle(request, response);
    }
}
