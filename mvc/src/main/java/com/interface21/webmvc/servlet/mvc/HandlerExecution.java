package com.interface21.webmvc.servlet.mvc;

import java.lang.reflect.Method;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;

public class HandlerExecution {

    private final Object controllerInstance;
    private final Method method;

    public HandlerExecution(Object controllerInstance, Method method) {
        this.controllerInstance = controllerInstance;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(controllerInstance, request, response);
    }
}
