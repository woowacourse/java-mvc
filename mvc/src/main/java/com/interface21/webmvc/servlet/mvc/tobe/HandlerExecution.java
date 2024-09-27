package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;
    private final Object instance;

    public HandlerExecution(final Method method) throws Exception {
        this.method = method;
        this.instance = getInstance(method);
    }

    private Object getInstance(Method method)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return method.getDeclaringClass().getDeclaredConstructor().newInstance();
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response)
            throws IllegalAccessException, InvocationTargetException {
        return (ModelAndView) method.invoke(instance, request, response);
    }
}
