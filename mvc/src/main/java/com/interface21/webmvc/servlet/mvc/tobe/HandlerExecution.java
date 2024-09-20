package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

public class HandlerExecution {

    private final List<Class<?>> REQUIRED_PARAMETER = List.of(HttpServletRequest.class, HttpServletResponse.class);

    private final Object executionTarget;
    private final Method targetMethod;

    public HandlerExecution(Object executionTarget, Method targetMethod) {
        validateMethodForm(targetMethod);
        this.executionTarget = executionTarget;
        this.targetMethod = targetMethod;
    }

    private void validateMethodForm(Method targetMethod) {
        Class<?> returnType = targetMethod.getReturnType();
        Class<?>[] parameterTypes = targetMethod.getParameterTypes();
        validateReturnType(returnType);
        validateParameterType(parameterTypes);
        validateParameters(parameterTypes);
    }

    private void validateReturnType(Class<?> returnType) {
        if (!returnType.equals(ModelAndView.class)) {
            throw new IllegalArgumentException("return type 이 다른 메서드를 Mapping 했습니다.");
        }
    }

    private void validateParameterType(Class<?>[] parameterTypes) {
        if (parameterTypes.length != 2) {
            throw new IllegalArgumentException("파리미터 개수가 다릅니다.");
        }
    }

    private void validateParameters(Class<?>[] parameterTypes) {
        for (Class<?> parameterType : parameterTypes) {
            validateParameter(parameterType);
        }
    }

    private void validateParameter(Class<?> parameterType) {
        if (!REQUIRED_PARAMETER.contains(parameterType)) {
            throw new IllegalArgumentException("지원하지 않는 파라미터 양식입니다.");
        }
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) targetMethod.invoke(executionTarget, request, response);
    }
}
