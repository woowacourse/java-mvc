package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class HandlerExecution {
    private final Class<?> controller;
    private final Method method;

    public HandlerExecution(Class<?> controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Constructor<?> constructor = controller.getDeclaredConstructor();
        makeAccessible(constructor);
        Object controllerInstance = constructor.newInstance();
        return (ModelAndView) method.invoke(controllerInstance, request, response);
    }

    private void makeAccessible(Constructor<?> constructor) {
        if (!constructor.canAccess(null)) {
            constructor.setAccessible(true);
        }
    }
}
