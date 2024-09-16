package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Class<?> aClass;
    private final Method method;

    public HandlerExecution(Class<?> aClass, Method method) {
        this.aClass = aClass;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Constructor<?> constructor = aClass.getConstructor();
        Object controller = constructor.newInstance();
        return (ModelAndView) method.invoke(controller, request, response);
    }
}
