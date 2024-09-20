package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HandlerExecution {
    private final Object controller;
    private final Method method;

    public HandlerExecution(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Class<?>[] parameterTypes = method.getParameterTypes();
        List<Object> args = new ArrayList<>();

        for (Class<?> parameterType : parameterTypes) {
            if (parameterType.isAssignableFrom(HttpServletRequest.class)) {
                args.add(request);
            }
            if (parameterType.isAssignableFrom(HttpServletResponse.class)) {
                args.add(response);
            }
        }

        return (ModelAndView) method.invoke(controller, args.toArray());
    }
}
