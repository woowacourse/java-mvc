package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Class<?> controller;
    private final Method method;

    public HandlerExecution(Class<?> controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        // request 의 uri, method 를 처리하는 TestController 메서드 실행
        Object controllerInstance = controller.getDeclaredConstructor().newInstance();

        return (ModelAndView) method.invoke(controllerInstance, request, response);
    }
}
