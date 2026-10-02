package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object controller;
    private final Method method;

    public HandlerExecution(final Object controller, final Method method) {
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        try {
            return (ModelAndView) method.invoke(controller, request, response);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException("컨트롤러 메서드 실행 중 예외 발생: " + method.toGenericString(), e.getCause());
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("컨트롤러 메서드 호출 실패: " + method.toGenericString(), e);
        }
    }
}
