package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class HandlerExecution {

    private final Object instance;
    private final Method method;

    public HandlerExecution(final Object instance, final Method method) {
        this.instance = instance;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Parameter[] parameters = method.getParameters();
        final List<Object> args = new ArrayList<>();
        for (final Parameter parameter : parameters) {
            if (parameter.getType().equals(HttpServletRequest.class)) {
                args.add(request);
            } else if (parameter.getType().equals(HttpServletResponse.class)) {
                args.add(response);
            }
        }

        return (ModelAndView) method.invoke(instance, args.toArray());
    }
}
