package com.interface21.webmvc.servlet;

import com.interface21.core.SingletonBeanContainer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;

    public HandlerExecution(Method method) {
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response)
            throws InvocationTargetException, IllegalAccessException {

        SingletonBeanContainer container = SingletonBeanContainer.getInstance();
        Class<?> controllerClass = method.getDeclaringClass();
        Object controller = container.getTypedBean(controllerClass);
        return (ModelAndView) method.invoke(controller, request, response);
    }
}
