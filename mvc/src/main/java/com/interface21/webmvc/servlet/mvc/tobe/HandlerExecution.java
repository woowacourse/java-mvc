package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;

import java.lang.reflect.Method;
import java.util.Arrays;

public class HandlerExecution {

    private final Object controllerInstance;
    private final Method handlerMethod;

    public HandlerExecution(Object controllerInstance, Method handlerMethod) {
        validateHandlerMethod(handlerMethod);

        this.controllerInstance = controllerInstance;
        this.handlerMethod = handlerMethod;
    }

    private void validateHandlerMethod(Method handlerMethod) {
        validateParameterTypes(handlerMethod);
        validateReturnType(handlerMethod);
    }

    private void validateParameterTypes(Method handlerMethod) {
        Class<?>[] expectedParameterTypes = {
                HttpServletRequest.class,
                HttpServletResponse.class
        };

        boolean hasSupportedParameters = Arrays.equals(
                handlerMethod.getParameterTypes(),
                expectedParameterTypes
        );

        if (!hasSupportedParameters) {
            throw new IllegalArgumentException(
                    "지원하지 않는 컨트롤러 메서드: "
                            + handlerMethod.toGenericString()
                            + ". 매개변수는 (HttpServletRequest, HttpServletResponse)여야 합니다."
            );
        }
    }

    private void validateReturnType(Method handlerMethod) {
        boolean returnsModelAndView = ModelAndView.class.isAssignableFrom(
                handlerMethod.getReturnType()
        );

        if (!returnsModelAndView) {
            throw new IllegalArgumentException(
                    "지원하지 않는 컨트롤러 메서드: "
                            + handlerMethod.toGenericString()
                            + ". 반환 타입은 ModelAndView 또는 그 하위 타입이어야 합니다."
            );
        }
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object result = handlerMethod.invoke(controllerInstance, request, response);
        return (ModelAndView) result;
    }
}
