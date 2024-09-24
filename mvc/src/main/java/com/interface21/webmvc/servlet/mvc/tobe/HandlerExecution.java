package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object classInstance;
    private final Method method;

    public HandlerExecution(Object classInstance, Method method) {
        this.classInstance = classInstance;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            return (ModelAndView) method.invoke(classInstance, request, response);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("메서드에 접근할 수 없는 상태입니다.");
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
