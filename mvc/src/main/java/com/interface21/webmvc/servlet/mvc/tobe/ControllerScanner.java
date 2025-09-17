package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> instantiateControllers() {
        return reflections.getTypesAnnotatedWith(Controller.class).stream()
                .collect(Collectors.toMap(clazz -> clazz, this::instantiate));
    }

    private Object instantiate(Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            log.error("Failed to instantiate controller: " + clazz, e);
            throw new IllegalStateException();
        }
    }

    public Map<HandlerKey, HandlerExecution> scanHandlerExecutions(Map<Class<?>, Object> controllers) {
        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();

        controllers.forEach((clazz, instance) ->
                ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class))
                        .forEach(method -> registerHandler(handlerExecutions, instance, method))
        );

        return handlerExecutions;
    }

    private void registerHandler(Map<HandlerKey, HandlerExecution> handlerExecutions,
                                 Object controllerInstance,
                                 Method method) {

        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        HandlerExecution execution = new HandlerExecution(controllerInstance, method);

        Arrays.stream(resolveHttpMethods(mapping))
                .map(httpMethod -> new HandlerKey(mapping.value(), httpMethod))
                .forEach(key -> handlerExecutions.put(key, execution));
    }

    private RequestMethod[] resolveHttpMethods(RequestMapping mapping) {
        return mapping.method().length == 0 ? RequestMethod.values() : mapping.method();
    }
}
