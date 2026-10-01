package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
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

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        try {
            return (ModelAndView) method.invoke(controller, request, response);
        } catch (InvocationTargetException invocationException) {
            final var throwable = invocationException.getCause();
            if (throwable instanceof Exception controllerException) {
                throw controllerException;
            }
            if (throwable instanceof Error error) {
                throw error;
            }
            throw new IllegalStateException("Controller invocation failed", throwable);
        }
    }
}
