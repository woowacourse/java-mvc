package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;

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
        final var result = method.invoke(controller, request, response);

        return (ModelAndView) result;
    }
}
