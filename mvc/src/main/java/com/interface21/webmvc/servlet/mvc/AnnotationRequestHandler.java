package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AnnotationRequestHandler implements RequestHandler {

    private final Method method;
    private final Object controllerInstance;

    public AnnotationRequestHandler(Method method, Class<?> controllerClass) throws ReflectiveOperationException {
        this.method = method;
        this.controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response)
            throws ReflectiveOperationException {
        return (ModelAndView) method.invoke(controllerInstance, request, response);
    }
}
