package com.interface21.webmvc.servlet.mvc.tobe.handleradaptor;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.returnvaluehandler.ReturnValueHandlers;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutionHandlerAdapter implements HandlerAdapter {

    private static final ReturnValueHandlers returnvalueHandlers = new ReturnValueHandlers();

    @Override
    public boolean support(Object handler) {
        return (handler instanceof HandlerExecution);
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Object returnValue = ((HandlerExecution) handler).handle(request, response);
        return returnvalueHandlers.handler(returnValue);
    }
}
