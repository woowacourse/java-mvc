package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<Class<?>, Object> controllers;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.controllers = new HashMap<>();
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final var controllerScanner = new ControllerScanner(basePackage);
        controllers.putAll(controllerScanner.getControllers());
        controllers.forEach(this::registerHandlers);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandlers(final Class<?> controllerClass, final Object controller) {
        Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .forEach(method -> registerHandler(controller, method, method.getAnnotation(RequestMapping.class)));
    }

    private void registerHandler(final Object controller, final Method method, final RequestMapping requestMapping) {
        final var handlerExecution = new HandlerExecution(controller, method);
        final RequestMethod[] requestMethods = requestMapping.method().length == 0
                ? RequestMethod.values()
                : requestMapping.method();

        for (RequestMethod requestMethod : requestMethods) {
            final var handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final var requestMethod = RequestMethod.valueOf(request.getMethod());
        final var handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
