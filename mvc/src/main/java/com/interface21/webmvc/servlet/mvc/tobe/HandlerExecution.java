package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Class<?> controllerClass;
    private final Method method;

    public HandlerExecution(Class<?> controllerClass, Method method) {
        this.controllerClass = controllerClass;
        this.method = method;
    }

    public ModelAndView handle(
            final HttpServletRequest request, final HttpServletResponse response
    ) throws IllegalArgumentException, ReflectiveOperationException {
        Object controller = controllerClass.getDeclaredConstructor().newInstance();
        try {
            return (ModelAndView) method.invoke(controller, request, response);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new IllegalArgumentException("Failed to invoke controller method", e);
        }
    }
}
