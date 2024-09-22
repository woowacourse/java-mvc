package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.ModelAndView;

public class HandlerExecution {

    private static final int EXPECTED_PARAMETER_COUNT = 2;
    private static final int REQUEST_PARAMETER_INDEX = 0;
    private static final int RESPONSE_PARAMETER_INDEX = 1;

    private final Method handler;

    public HandlerExecution(final Method handler) {
        this.handler = handler;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Class<?> controllerClass = handler.getDeclaringClass();
        Constructor<?> constructor = controllerClass.getConstructor();
        Object controller = constructor.newInstance();
        Object[] httpParameters = requireHttpParameters(request, response);
        return (ModelAndView) handler.invoke(controller, httpParameters);
    }

    private Object[] requireHttpParameters(final HttpServletRequest request, final HttpServletResponse response) {
        Class<?>[] types = handler.getParameterTypes();
        validateParameterTypes(types);
        return new Object[]{request, response};
    }

    private void validateParameterTypes(final Class<?>[] types) {
        if (types.length != EXPECTED_PARAMETER_COUNT) {
            throw new IllegalArgumentException("Invalid number of request parameters.");
        }
        if (!isHttpRequestAndResponse(types)) {
            throw new IllegalArgumentException("Invalid request parameter types.");
        }
    }

    private boolean isHttpRequestAndResponse(final Class<?>[] types) {
        return types[REQUEST_PARAMETER_INDEX].equals(HttpServletRequest.class)
                && types[RESPONSE_PARAMETER_INDEX].equals(HttpServletResponse.class);
    }
}
