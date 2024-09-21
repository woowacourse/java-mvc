package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import org.reflections.Reflections;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

public class HandlerExecution {

    private final Object[] basePackage;

    public HandlerExecution(Object[] basePackage) {
        this.basePackage = basePackage;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Set<Class<?>> annotatedClasses = checkControllerAnnotationClass();

        return annotatedClasses.stream()
                .map(this::constructController)
                .flatMap(controller -> Arrays.stream(controller.getClass().getDeclaredMethods())
                        .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                        .filter(method -> isMatchingRequest(request, method))
                        .map(method -> invokeMethod(method, controller, request, response)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid request"));
    }

    private Set<Class<?>> checkControllerAnnotationClass() {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
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
