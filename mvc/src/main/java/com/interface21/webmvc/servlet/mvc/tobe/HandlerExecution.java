package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {
    private final Class<?> controller;
    private final Method method;

    public HandlerExecution(Class<?> controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            Constructor<?> constructor = controller.getDeclaredConstructor();
            makeAccessible(constructor);
            Object controllerInstance = constructor.newInstance();
            return (ModelAndView) method.invoke(controllerInstance, request, response);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException());
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("컨트롤러에 기본 생성자가 없습니다: " + controller.getName());
        } catch (InstantiationException e) {
            throw new IllegalStateException("컨트롤러 인스턴스를 생성할 수 없습니다: " + controller.getName());
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("컨트롤러 생성자에 접근이 불가합니다: " + controller.getName());
        } catch (ClassCastException e) {
            throw new IllegalStateException(
                    "컨트롤러 메서드 반환 타입이 ModelAndView가 아닙니다: " + controller.getName() + "#" + method.getName());
        }
    }

    private void makeAccessible(Constructor<?> constructor) {
        if (!constructor.canAccess(null)) {
            constructor.setAccessible(true);
        }
    }
}
