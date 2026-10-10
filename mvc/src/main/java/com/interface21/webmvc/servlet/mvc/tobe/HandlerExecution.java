package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class HandlerExecution {
    private final Object handler;
    private final Method method;

    public HandlerExecution(Object handler, Method method) {
        validateSignature(method);
        this.handler = handler;
        this.method = method;
    }

    private void validateSignature(Method method) {
        if (!Modifier.isPublic(method.getModifiers()) || !hasServletParameters(method) || !returnsModelAndView(method)) {
            throw new IllegalStateException(
                    "핸들러 메서드는 public ModelAndView (HttpServletRequest, HttpServletResponse) 형태여야 합니다: " + method);
        }
    }

    private boolean hasServletParameters(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        return parameterTypes.length == 2
                && parameterTypes[0].isAssignableFrom(HttpServletRequest.class)
                && parameterTypes[1].isAssignableFrom(HttpServletResponse.class);
    }

    private boolean returnsModelAndView(Method method) {
        return ModelAndView.class.isAssignableFrom(method.getReturnType());
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(handler, request, response);
    }

    @Override
    public String toString() {
        return method.getDeclaringClass().getName() + "#" + method.getName();
    }
}
