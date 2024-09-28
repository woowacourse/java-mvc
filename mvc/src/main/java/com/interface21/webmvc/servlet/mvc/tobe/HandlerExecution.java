package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.ModelAndView;

public class HandlerExecution {

    private final Object handler;
    private final Method method;

    public HandlerExecution(final Object handler, final Method method) {
        this.handler = handler;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response)
            throws IllegalAccessException, InvocationTargetException {
        return (ModelAndView) method.invoke(handler, request, response);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final HandlerExecution that)) {
            return false;
        }
        return Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(method);
    }

    @Override
    public String toString() {
        return "HandlerExecution{" +
               "method=" + method.getName() +
               '}';
    }
}
