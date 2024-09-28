package com.interface21.webmvc.servlet.mvc;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.exception.HandlerMappingInitializeException;
import com.interface21.webmvc.servlet.handler.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final String[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final String... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        reflections.getTypesAnnotatedWith(Controller.class)
                .stream()
                .forEach(this::addHandlerExecution);
    }

    private void addHandlerExecution(Class controllerClass) {
        Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> addHandlerExecution(controllerClass, method));
    }

    private void addHandlerExecution(Class clazz, Method method) {
        try {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            String paths = requestMapping.value();
            RequestMethod[] requestMethods = requestMapping.method();
            Object instance = clazz.getDeclaredConstructor().newInstance();
            HandlerExecution handlerExecution = new HandlerExecution(instance, method);
            log.info("Path : {}, Method : {}, Controller : {}",
                    paths, Arrays.toString(requestMethods), method.getName());

            Arrays.stream(requestMethods)
                    .map(requestMethod -> new HandlerKey(paths, requestMethod))
                    .forEach(handlerKey -> handlerExecutions.put(handlerKey, handlerExecution));
        } catch (Exception e) {
            throw new HandlerMappingInitializeException(e);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), request.getMethod());
        return handlerExecutions.get(handlerKey);
    }
}
