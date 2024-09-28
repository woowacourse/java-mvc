package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;
    private final Object instance;

    public HandlerExecution(final Method method) {
        this.method = method;
        this.instance = getInstance(method);
    }

    private Object getInstance(final Method method) {
        try {
            return this.getDeclaredConstructor(method).newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            throw new HandlerInstanceCreationFailedException(method, ex);
        }
    }

    private Constructor<?> getDeclaredConstructor(final Method method) {
        try {
            return method.getDeclaringClass().getDeclaredConstructor();
        } catch (NoSuchMethodException ex) {
            throw new HandlerConstructorNotFoundException(method, ex);
        }
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            return (ModelAndView) method.invoke(instance, request, response);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new HandlerExecutionFailedException(instance, method, ex);
        }
    }
}
