package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.registry.ControllerRegistry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;

    public HandlerExecution(Method method) {
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
        Class<?> clazz = method.getDeclaringClass();
        Object controller = ControllerRegistry.getOrCreateController(clazz);

        try {
            return (ModelAndView) method.invoke(controller, request, response);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("메서드를 실행하던 도중 에러가 발생했습니다.");
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("해당하는 메서드에 접근할 수 없습니다.");
        }
    }
}
