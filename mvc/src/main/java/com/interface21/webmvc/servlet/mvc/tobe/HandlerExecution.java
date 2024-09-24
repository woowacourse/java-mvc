package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {
    private final Object controller;
    private final Method method;

    public HandlerExecution(final Object controller, final Method method) {
        this.controller = controller;
        this.method = method;
    }

    public Object handle(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            return method.invoke(controller, request, response);
        } catch (final IllegalAccessException | InvocationTargetException e) {
            throw new FailHandlerExecuteException(method, e);
        }
    }
}
