package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class HandlerExecution {

    private final Object controller;
    private final Method method;

    public HandlerExecution(final Object controller, final Method method) {
        validateMethod(method);
        this.controller = controller;
        this.method = method;
    }

    private void validateMethod(final Method method) {
        final Class<?>[] expectedParameterTypes = {
                HttpServletRequest.class, HttpServletResponse.class
        };
        if (!Arrays.equals(method.getParameterTypes(), expectedParameterTypes)) {
            throw new IllegalArgumentException(
                    "컨트롤러 메서드의 인자는 (HttpServletRequest, HttpServletResponse)여야 합니다: "
                            + method.toGenericString());
        }
        if (!ModelAndView.class.isAssignableFrom(method.getReturnType())) {
            throw new IllegalArgumentException(
                    "컨트롤러 메서드의 반환 타입은 ModelAndView여야 합니다: " + method.toGenericString());
        }
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final ModelAndView result;
        try {
            result = (ModelAndView) method.invoke(controller, request, response);
        } catch (InvocationTargetException e) {
            final var cause = e.getCause();
            if (cause instanceof Error error) {
                throw error;
            }
            throw new IllegalStateException(
                    "컨트롤러 메서드 실행에 실패했습니다: " + method.toGenericString(), cause);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(
                    "컨트롤러 메서드에 접근할 수 없습니다: " + method.toGenericString(), e);
        }
        if (result == null) {
            throw new IllegalStateException(
                    "컨트롤러 메서드는 ModelAndView를 반환해야 합니다: " + method.toGenericString());
        }
        return result;
    }
}
