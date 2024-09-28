package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.ModelAndView;

public class HandlerExecution {
    private final Object instance;
    private final Method method;

    public HandlerExecution(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            return (ModelAndView) method.invoke(instance, request, response);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("메서드에 접근할 수 없습니다: " + method.getName(), e);
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            throw new RuntimeException("메서드 호출 중 오류가 발생했습니다: " + method.getName() +
                    ", 원인: " + targetException.getMessage(), targetException);
        }
    }
}
