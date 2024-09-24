package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
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
                .map(parameterType -> resolveArgument(request, response, parameterType))
                .toArray();

        return (ModelAndView) method.invoke(controller, args);
    }

    private Object resolveArgument(HttpServletRequest request, HttpServletResponse response, Class<?> parameterType) {
        if (ServletRequest.class.isAssignableFrom(parameterType)) {
            return request;
        }
        if (ServletResponse.class.isAssignableFrom(parameterType)) {
            return response;
        }

        throw new IllegalStateException("Unsupported parameter type: " + parameterType.getName());
    }
}
