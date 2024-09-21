package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;

    public HandlerExecution(Method method) {
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws
            InvocationTargetException,
            IllegalAccessException,
            NoSuchMethodException,
            InstantiationException {
        Class<?> declaringClass = method.getDeclaringClass();
        Constructor<?> constructor = declaringClass.getConstructor();
        Object controller = constructor.newInstance();
        return (ModelAndView) method.invoke(controller, request, response);
    }
}
