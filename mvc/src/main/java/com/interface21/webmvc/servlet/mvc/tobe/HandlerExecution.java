package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {
    private final Object controllerInstance;
    private final Method method;

    public HandlerExecution(Object controllerInstance, Method method) {
        this.controllerInstance = controllerInstance;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            return (ModelAndView) method.invoke(controllerInstance, request, response);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("메서드에 접근이 불가합니다: " + method.getName());
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException());
        } catch (ClassCastException e) {
            throw new IllegalStateException("메서드 반환 타입이 ModelAndView가 아닙니다: " + method.getName());
        }
    }
}
