package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Class<?> controllerClass;
    private final Method method;

    public HandlerExecution(final Class<?> controllerClass, final Method method) {
        this.controllerClass = controllerClass;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Object controller = controllerClass.getDeclaredConstructor().newInstance();
        final Object returnValue = method.invoke(controller, request, response);
        if (returnValue instanceof ModelAndView modelAndView) {
            return modelAndView;
        }
        throw new IllegalStateException("Unexpected return type: " + returnValue);
    }
}
