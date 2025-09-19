package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object controller;
    private final Method handlerMethod;

    public HandlerExecution(final Object controller, final Method handlerMethod) {
        this.controller = controller;
        this.handlerMethod = handlerMethod;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            return (ModelAndView) handlerMethod.invoke(controller, request, response);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to handle: target method is inaccessible.", e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException("An unexpected exception occurred during method invocation.", e);
        }
    }
}
