package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object controllerInstance;
    private final Method handlerMethod;

    public HandlerExecution(Object controllerInstance, Method handlerMethod) {
        this.controllerInstance = controllerInstance;
        this.handlerMethod = handlerMethod;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object result = handlerMethod.invoke(controllerInstance, request, response);
        return (ModelAndView) result;
    }
}
