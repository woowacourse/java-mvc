package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;

public class HandlerExecution {

    private final Object clazz;
    private final Method method;

    public HandlerExecution(Class<?> clazz, Method method) {
        try {
            this.clazz = clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(String.format(
                    "컨트롤러 %s 를 생성할 수 있는 기본 생성자가 존재하지 않습니다.", clazz.getName())
            );
        }
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(clazz, request, response);
    }
}
