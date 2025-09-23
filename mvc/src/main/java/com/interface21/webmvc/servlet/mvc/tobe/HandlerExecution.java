package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecution {

    private final Object controllerInstance;
    private final Method handlerMethod;

    public HandlerExecution(Object controllerInstance, Method handlerMethod) {
        handlerMethod.setAccessible(true);
        validateMethodParameters(handlerMethod);
        this.controllerInstance = controllerInstance;
        this.handlerMethod = handlerMethod;
    }

    private static void validateMethodParameters(Method handlerMethod) {
        // 파라미터에 request, response 외에 다른 타입이 있으면 예외 발생
        Class<?>[] parameterTypes = handlerMethod.getParameterTypes();
        if (parameterTypes.length != 2 ||
            !HttpServletRequest.class.isAssignableFrom(parameterTypes[0]) ||
            !HttpServletResponse.class.isAssignableFrom(parameterTypes[1])) {
            throw new IllegalArgumentException("Handler method must have exactly two parameters: HttpServletRequest and HttpServletResponse");
        }

        // 반환 타입이 String 또는 ModelAndView가 아니면 예외 발생
        Class<?> returnType = handlerMethod.getReturnType();
        if (!String.class.isAssignableFrom(returnType) && !ModelAndView.class.isAssignableFrom(returnType)) {
            throw new IllegalArgumentException("Handler method must return String or ModelAndView");
        }
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object result = handlerMethod.invoke(controllerInstance, request, response);

        if (result instanceof String) {
            String viewName = (String) result;
            return new ModelAndView(new JspView(viewName));
        } else if (result instanceof ModelAndView) {
            return (ModelAndView) result;
        }

        throw new IllegalArgumentException("Handler method must return String or ModelAndView");
    }
}
