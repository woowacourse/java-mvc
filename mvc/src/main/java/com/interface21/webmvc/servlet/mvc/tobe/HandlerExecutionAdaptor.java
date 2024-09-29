package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.servlet.HandlerAdaptor;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.InvocationTargetException;

public class HandlerExecutionAdaptor implements HandlerAdaptor {

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        HandlerExecution handlerExecution = (HandlerExecution) handler;

        return handlerExecution.handle(request, response);
    }

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }
}
