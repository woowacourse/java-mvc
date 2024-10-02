package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class HandlerExecution {

    private final Object clazz;
    private final Method method;

    public HandlerExecution(Object clazz, Method method) {
        validateMethod(method);
        this.clazz = clazz;
        this.method = method;
    }

    private void validateMethod(Method method) {
        Parameter[] parameters = method.getParameters();
        validateParameterSize(parameters);
        validateRequestParameter(parameters);
        validateResponseParameter(parameters);
        validateReturnType(method);
    }

    private void validateParameterSize(Parameter[] parameters) {
        if (parameters.length != 2) {
            throw new IllegalArgumentException("Invalid method parameter size: " + parameters.length);
        }
    }

    private void validateRequestParameter(Parameter[] parameters) {
        if (parameters[0].getType() != HttpServletRequest.class) {
            throw new IllegalArgumentException("Invalid method parameter type: " + parameters[0].getType());
        }
    }

    private void validateResponseParameter(Parameter[] parameters) {
        if (parameters[1].getType() != HttpServletResponse.class) {
            throw new IllegalArgumentException("Invalid method parameter type: " + parameters[1].getType());
        }
    }

    private void validateReturnType(Method method) {
        Class<?> returnType = method.getReturnType();
        if (!returnType.isAssignableFrom(ModelAndView.class)) {
            throw new IllegalArgumentException("Invalid method return type: " + returnType);
        }
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(clazz, request, response);
    }
}
