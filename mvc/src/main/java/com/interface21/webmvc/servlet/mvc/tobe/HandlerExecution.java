package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {
    private final Object controller;
    private final Method method;

    public HandlerExecution(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i].isAssignableFrom(HttpServletRequest.class)) {
                args[i] = request;
            }
            if (parameterTypes[i].isAssignableFrom(HttpServletResponse.class)) {
                args[i] = response;
            }
        }

        return (ModelAndView) method.invoke(controller, args);
    }
}
