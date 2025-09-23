package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object controller;
    private final Method method;

    public HandlerExecution(final Class<?> clazz, final Method method) {
        try {
            this.controller = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create controller instance: " + clazz.getName(), e);
        }
        this.method = method;
    }

    public HandlerExecution(final Object controller, final Method method) {
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final var invoked = method.invoke(controller, request, response);
        return (ModelAndView) invoked;
    }
}
