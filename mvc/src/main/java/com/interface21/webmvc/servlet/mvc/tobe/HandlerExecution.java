package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HandlerExecution {

    private final Object controller;
    private final Method method;

    public HandlerExecution(final Object controller, final Method method) {
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Object[] arguments = resolveArguments(request, response);
        return (ModelAndView) method.invoke(controller, arguments);
    }

    private Object[] resolveArguments(final HttpServletRequest request, final HttpServletResponse response) {
        final Map<Class<?>, Object> providedArguments = new HashMap<>();
        providedArguments.put(HttpServletRequest.class, request);
        providedArguments.put(HttpServletResponse.class, response);

        final Class<?>[] parameterTypes = method.getParameterTypes();
        return Arrays.stream(parameterTypes)
                .map(providedArguments::get)
                .toArray();
    }

    public Method getMethod() {
        return method;
    }
}
