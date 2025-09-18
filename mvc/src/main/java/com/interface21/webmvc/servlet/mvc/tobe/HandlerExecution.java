package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object target;
    private final Method method;

    public HandlerExecution(final Object target, final Method method) {
        validateMethod(method, target);
        this.target = target;
        this.method = method;
    }

    private void validateMethod(final Method method, final Object target) {
        if (method.getReturnType() != ModelAndView.class) {
            throw new IllegalArgumentException("HandlerExecution의 메서드는 ModelAndView를 반환해야 합니다.");
        }

        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != 2 ||
                parameterTypes[0] != HttpServletRequest.class ||
                parameterTypes[1] != HttpServletResponse.class) {
            throw new IllegalArgumentException(
                    "HandlerExecution은 HttpServletRequest와 HttpServletResponse만을 매개변수로 받아야 합니다."
            );
        }

        if (!method.canAccess(target)) {
            throw new IllegalArgumentException("HandlerExecution의 메서드는 public이어야 합니다.");
        }
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(target, request, response);
    }
}
