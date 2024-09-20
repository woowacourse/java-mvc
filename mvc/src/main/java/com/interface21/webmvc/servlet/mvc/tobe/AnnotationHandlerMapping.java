package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.annotation.Annotation;
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

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        try {
            Arrays.stream(basePackage).forEach(this::reflectControllers);
        } catch (Exception e) {

        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void reflectControllers(Object basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        List<Method> handlers = classes.stream()
                .map(Class::getDeclaredMethods)
                .flatMap(Arrays::stream)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();

        handlers.forEach(this::putHandlerExecutions);
    }

    private void putHandlerExecutions(Method method) {
        Annotation annotation = method.getAnnotation(RequestMapping.class);
        String uri = (String) getValueFromAnnotationMethod(annotation, "value");
        RequestMethod[] requestMethods = (RequestMethod[]) getValueFromAnnotationMethod(annotation, "method");
        HandlerExecution handlerExecution = new HandlerExecution(method);
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private static Object getValueFromAnnotationMethod(Annotation annotation, String method) {
        try {
            return annotation.getClass()
                    .getMethod(method)
                    .invoke(annotation);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Reflect Annotation Error");
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
