package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;
    private final Class<?> controllerClass;

    public HandlerExecution(Method method, Class<?> controllerClass) {
        this.method = method;
        this.controllerClass = controllerClass;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
        return (ModelAndView) method.invoke(controllerInstance, request, response);
    }
}
