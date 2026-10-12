package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object handler;
    private final Method method;

    public HandlerExecution(final Object handler, final Method method) {
        this.handler = handler;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        try {
            return (ModelAndView) method.invoke(handler, request, response);
        } catch (final InvocationTargetException e) {
            throw unwrap(e);
        }
    }

    private Exception unwrap(final InvocationTargetException e) {
        final Throwable cause = e.getCause();
        if (cause instanceof Error error) {
            throw error;
        }
        if (cause instanceof Exception exception) {
            return exception;
        }
        return e;
    }
}
