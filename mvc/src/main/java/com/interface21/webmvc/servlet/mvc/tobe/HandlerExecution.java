package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;

    public HandlerExecution(Method method) {
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response)
            throws IllegalAccessException, InstantiationException, ExceptionInInitializerError,
            SecurityException, InvocationTargetException, NoSuchMethodException {
        Constructor<?> constructor = method.getDeclaringClass().getConstructor();
        Object newInstance = constructor.newInstance();

        return (ModelAndView) method.invoke(newInstance, request, response);
    }
}
