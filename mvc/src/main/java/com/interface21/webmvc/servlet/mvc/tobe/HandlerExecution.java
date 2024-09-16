package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecution {

    private final Method method;
    private final Object instance;

    public HandlerExecution(Method method) {
        this.method = method;
        this.instance = createInstance(method);
    }

    private Object createInstance(Method method) {
        try {
            return method.getDeclaringClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws
            Exception {
        return (ModelAndView) method.invoke(instance, request, response);
    }
}
