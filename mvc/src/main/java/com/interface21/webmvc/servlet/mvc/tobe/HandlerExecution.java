package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object controllerInstance;
    private final Method method;

    public HandlerExecution(Object controller, Method method) {
        this.controllerInstance = controller;
        this.method = method;
    }

    public ModelAndView handle(
            final HttpServletRequest request, final HttpServletResponse response
    ) throws IllegalArgumentException {
        try {
            return (ModelAndView) method.invoke(controllerInstance, request, response);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new IllegalArgumentException("Failed to invoke controller method", e);
        }
    }
}
