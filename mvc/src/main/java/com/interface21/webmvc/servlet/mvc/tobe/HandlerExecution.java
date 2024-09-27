package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerExecution {

    private static final Logger log = LoggerFactory.getLogger(HandlerExecution.class);

    private final Class<?> controller;
    private final Object controllerInstance;
    private final Method method;

    public HandlerExecution(Method method)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.method = method;
        this.controller = method.getDeclaringClass();
        this.controllerInstance = this.controller.getDeclaredConstructor().newInstance();
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        // request 의 uri, method 를 처리하는 Controller 메서드 실행
        return (ModelAndView) method.invoke(controllerInstance, request, response);
    }

    @Override
    public String toString() {
        return "HandlerExecution{" +
                "controller=" + controller +
                ", controllerInstance=" + controllerInstance +
                ", method=" + method +
                '}';
    }
}
