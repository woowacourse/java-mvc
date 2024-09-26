package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.reflections.Reflections;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HandlerExecution {

    private final Object[] basePackage;
    private final List<Object> controllers;

    public HandlerExecution(Object[] basePackage) {
        this.basePackage = basePackage;
        this.controllers = createController();
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return controllers.stream()
                .flatMap(controller -> Arrays.stream(controller.getClass().getDeclaredMethods())
                        .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                        .filter(method -> isMatchingRequest(request, method))
                        .map(method -> invokeMethod(method, controller, request, response)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid request"));
    }

    private List<Object> createController() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return annotatedClasses.stream()
                .map(this::constructController)
                .toList();
    }


    private Object constructController(Class<?> annotatedClass) {
        try {
            return annotatedClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create controller instance", e);
        }
    }

    private boolean isMatchingRequest(HttpServletRequest request, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        boolean isMatchURI = requestMapping.value().startsWith(request.getRequestURI());
        if (requestMapping.method() == null) {
            return isMatchURI;
        }
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        boolean isMatchMethod = Arrays.asList(requestMapping.method()).contains(requestMethod);
        return isMatchURI && isMatchMethod;
    }

    private ModelAndView invokeMethod(Method method, Object controller, HttpServletRequest request, HttpServletResponse response) {
        try {
            return (ModelAndView) method.invoke(controller, request, response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke controller method", e);
        }
    }
}
