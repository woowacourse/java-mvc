package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object controller;
    private final Method handlerMethod;

    public HandlerExecution(final Object controller, final Method handlerMethod) {
        this.controller = controller;
        this.handlerMethod = handlerMethod;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response){
        return (ModelAndView) handlerMethod.invoke(controller, request, response);
    }
}
