package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ModelAndViewHandlerExecutionAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        if (handler instanceof HandlerExecution handlerExecution) {
            return handlerExecution.getReturnType() == ModelAndView.class;
        }

        return false;
    }

    @Override
    public ModelAndView invoke(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerExecution handlerExecution = (HandlerExecution) handler;

        return (ModelAndView) handlerExecution.handle(request, response);
    }
}
