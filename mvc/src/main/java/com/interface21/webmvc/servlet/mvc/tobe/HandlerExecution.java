package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution {

    private final Class<?> controllerClass;
    private final Method method;

    public HandlerExecution(Class<?> controllerClass, Method method) {
        this.controllerClass = controllerClass;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object controller = controllerClass.getDeclaredConstructor().newInstance();
        return (ModelAndView) method.invoke(controller, request, response);
    }
}
