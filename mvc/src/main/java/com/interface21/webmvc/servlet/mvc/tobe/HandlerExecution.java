package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;

public class HandlerExecution {

    private final Object controller;
    private final Method method;

    public HandlerExecution(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] args = Arrays.stream(parameterTypes)
                .map(parameterType -> getArgument(request, response, parameterType))
                .toArray();

        return (ModelAndView) method.invoke(controller, args);
    }

    private Object getArgument(HttpServletRequest request, HttpServletResponse response, Class<?> parameterType) {
        if (parameterType.isAssignableFrom(HttpServletRequest.class)) {
            return request;
        }
        if (parameterType.isAssignableFrom(HttpServletResponse.class)) {
            return response;
        }
        throw new IllegalStateException("Unsupported parameter type: " + parameterType.getName());
    }
}
