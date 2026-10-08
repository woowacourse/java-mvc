package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class HandlerExecution {
    private final Object handler;
    private final Method method;

    public HandlerExecution(final Object handler, final Method method) {
        validateHandlerMethod(method);
        this.handler = handler;
        this.method = method;
    }

    private static void validateHandlerMethod(final Method method) {
        validateAccessModifier(method);
        validateParameterType(method);
        validateReturnType(method);
    }

    private static void validateReturnType(final Method method) {
        if (method.getReturnType() != ModelAndView.class) {
            throw new IllegalStateException(
                    "[ERROR] @RequestMapping 메서드는 ModelAndView를 반환해야 합니다. method: " + method.getDeclaringClass()
                            .getName() + "." + method.getName());
        }
    }

    private static void validateAccessModifier(final Method method) {
        if (!Modifier.isPublic(method.getModifiers())) {
            throw new IllegalStateException(
                    "[ERROR] @RequestMapping 메서드는 public이어야 합니다. method: " + method.getDeclaringClass().getName() + "."
                            + method.getName());
        }
    }

    private static void validateParameterType(final Method method) {
        Class<?>[] expected = {HttpServletRequest.class, HttpServletResponse.class};
        if (!Arrays.equals(method.getParameterTypes(), expected)) {
            throw new IllegalStateException(
                    "[ERROR] @RequestMapping 메서드의 파라미터는 (HttpServletRequest, HttpServletResponse)여야 합니다. method: "
                            + method.getDeclaringClass().getName() + "." + method.getName());
        }
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(handler, request, response); // handler.메서드(request, response) 와 같음
    }
}
