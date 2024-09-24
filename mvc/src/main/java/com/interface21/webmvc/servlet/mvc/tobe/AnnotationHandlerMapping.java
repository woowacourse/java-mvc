package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.techcourse.HandlerMapping;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        Set<Class<?>> controllerClasses = findControllerClasses();
        for (Class<?> controllerClass : controllerClasses) {
            registerHandlerMethods(controllerClass);
        }
    }

    private Set<Class<?>> findControllerClasses() {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void registerHandlerMethods(Class<?> controllerClass) {

        List<Method> newHandlerExecutions = Arrays.stream(controllerClass.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();

        Object controllerInstance = createControllerInstance(controllerClass);
        for (Method method : newHandlerExecutions) {
            List<HandlerKey> handlerKeys = createHandlerKeys(method);
            HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
            handlerKeys.forEach(handlerKey -> handlerExecutions.put(handlerKey, handlerExecution));
        }
    }

    private Object createControllerInstance(Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException(
                    "Failed to instantiate controller: " + controllerClass.getName(), e);
        }
    }

    private List<HandlerKey> createHandlerKeys(Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        return Arrays.stream(requestMapping.method())
                .map(requestMethod -> new HandlerKey(requestMapping.value(), requestMethod))
                .toList();
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey key = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(key);
    }
}
