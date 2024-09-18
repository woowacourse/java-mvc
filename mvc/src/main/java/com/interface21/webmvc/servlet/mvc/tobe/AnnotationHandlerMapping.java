package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class AnnotationHandlerMapping {

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        getAnnotatedClasses().forEach(this::registerHandlers);
    }

    private Set<Class<?>> getAnnotatedClasses() {
        return new Reflections(basePackage).getTypesAnnotatedWith(Controller.class);
    }

    private void registerHandlers(Class<?> clazz) {
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> registerHandler(method, clazz));
    }

    private void registerHandler(Method method, Class<?> clazz) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        Arrays.stream(requestMapping.method())
                .forEach(requestMethod -> putHandlerExecution(requestMapping.value(), requestMethod, method, clazz));
    }

    private void putHandlerExecution(String requestUri, RequestMethod requestMethod, Method method, Class<?> clazz) {
        HandlerKey handlerKey = new HandlerKey(requestUri, requestMethod);
        HandlerExecution handlerExecution = findHandlerExecution(method, clazz);
        validateExecutions(handlerKey);
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    private HandlerExecution findHandlerExecution(Method method, Class<?> clazz) {
        try {
            return new HandlerExecution(method, clazz);
        } catch (Exception e) {
            throw new IllegalArgumentException("Handler find error.");
        }
    }

    private void validateExecutions(HandlerKey handlerKey) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException("Handler already exists.");
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), request.getMethod()));
    }
}
