package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;
    private final Object instance;

    public HandlerExecution(Method method) {
        try {
            this.method = method;
            Class<?> declaringClass = method.getDeclaringClass();
            Constructor<?> constructor = declaringClass.getDeclaredConstructor();
            this.instance = constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("기본 생성자가 존재하지 않습니다");
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            return (ModelAndView) method.invoke(instance, request, response);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
}
