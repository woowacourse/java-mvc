package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class HandlerExecution {

    private final Object controllerInstance;
    private final Method method;

    public HandlerExecution(final Object controllerInstance, final Method method) {
        this.controllerInstance = controllerInstance;
        this.method = method;
    }

    public Object handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object[] args = resolveArguments(request, response);
        return method.invoke(controllerInstance, args);
    }

    private Object[] resolveArguments(final HttpServletRequest request, final HttpServletResponse response) {
        List<Object> args = new ArrayList<>();

        for (Parameter parameter : method.getParameters()) {
            args.add(resolveArgument(parameter, request, response));
        }

        return args.toArray();
    }

    private Object resolveArgument(final Parameter parameter, final HttpServletRequest request, final HttpServletResponse response) {
        Class<?> type = parameter.getType();

        if (type == HttpServletRequest.class) {
            return request;
        }
        if (type == HttpServletResponse.class) {
            return response;
        }
        if (parameter.isAnnotationPresent(RequestParam.class)) {
            String paramName = parameter.getAnnotation(RequestParam.class).value();
            return request.getParameter(paramName);
        }

        return null;
    }
}
