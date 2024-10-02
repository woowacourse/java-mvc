package com.interface21.webmvc.servlet;

import com.interface21.core.BeanContainer;
import com.interface21.core.BeanContainerFactory;
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

        BeanContainer container = BeanContainerFactory.getContainer();
        Class<?> controllerClass = method.getDeclaringClass();
        Object controller = container.getBean(controllerClass);
        return (ModelAndView) method.invoke(controller, request, response);
    }
}
