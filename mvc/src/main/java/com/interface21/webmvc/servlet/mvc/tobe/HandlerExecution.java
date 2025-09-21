package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private static final Logger log = LoggerFactory.getLogger(HandlerExecution.class);

    private final Object controller;
    private final Method method;

    public HandlerExecution(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
        makeMethodAccessible();
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            log.debug("Handler 메서드 실행: {}.{}", controller.getClass().getSimpleName(), method.getName());
            Object result = method.invoke(controller, request, response);

            if (result instanceof ModelAndView) {
                return (ModelAndView) result;
            } else {
                throw new IllegalStateException("Handler 메서드는 ModelAndView를 반환해야 합니다. 실제 반환 타입: " +
                    (result != null ? result.getClass().getName() : "null"));
            }
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            log.error("Handler 메서드 실행 중 예외 발생: {}.{}", controller.getClass().getSimpleName(), method.getName(), targetException);

            if (targetException instanceof Exception) {
                throw (Exception) targetException;
            } else {
                throw new RuntimeException("Handler 메서드 실행 실패", targetException);
            }
        } catch (IllegalAccessException e) {
            log.error("Handler 메서드 접근 권한 오류: {}.{}", controller.getClass().getSimpleName(), method.getName(), e);
            throw new RuntimeException("Handler 메서드에 접근할 수 없습니다", e);
        }
    }

    private void makeMethodAccessible() {
        if (!method.isAccessible()) {
            method.setAccessible(true);
            log.debug("메서드 접근 가능하도록 설정: {}.{}", controller.getClass().getSimpleName(), method.getName());
        }
    }
}
