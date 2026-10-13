package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object controller;
    private final Method method;

    public HandlerExecution(final Object controller, final Method method) {
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Object returnValue = method.invoke(controller, request, response);
        if (returnValue instanceof ModelAndView modelAndView) {
            return modelAndView;
        }
        if (returnValue instanceof String viewName) {
            return new ModelAndView(viewName);
        }
        throw new IllegalStateException("Unsupported handler return value: " + returnValue);
    }
}
