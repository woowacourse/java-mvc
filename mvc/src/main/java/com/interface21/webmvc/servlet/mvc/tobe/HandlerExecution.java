package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;
    private final Object controller;

    public HandlerExecution(Method method, Object controller) {
        this.method = method;
        this.controller = controller;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object object = method.invoke(controller, request, response);
        return (ModelAndView) object;
    }
}
