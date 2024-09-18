package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

public class HandlerExecution {

    private final Method method;

    public HandlerExecution(Method method) {
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object instance = findObjectByMethod(method);
        return (ModelAndView) method.invoke(instance, request, response);
    }

    private Object findObjectByMethod(Method method) throws Exception {
        Class<?> clazz = method.getDeclaringClass();
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        return constructor.newInstance();
    }
}
