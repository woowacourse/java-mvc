package com.interface21.webmvc.servlet.mvc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;

public class HandlerExecution {

    private final Object controllerInstance;
    private final Method method;

    public HandlerExecution(final Object controllerInstance, final Method method) {
        this.controllerInstance = controllerInstance;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            return (ModelAndView) method.invoke(controllerInstance, request, response);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("HandlerExecution 요청 처리 중 에러가 발생했습니다. " + e);
        }
    }
}
