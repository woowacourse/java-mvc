package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method controllerMethod;
    private final Object controller;

    public HandlerExecution(Object controller, Method controllerMethod) {
        this.controller = controller;
        this.controllerMethod = controllerMethod;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) controllerMethod.invoke(controller, request, response);
    }
}
