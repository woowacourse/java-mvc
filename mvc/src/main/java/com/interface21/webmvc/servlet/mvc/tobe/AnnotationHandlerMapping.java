package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        final var controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : controllerClasses) {
            registerHandlers(controllerClass);
        }

        log.info("Initialized AnnotationHandlerMapping with {} handlers", handlerExecutions.size());
    }

    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod;
        try {
            requestMethod = RequestMethod.valueOf(request.getMethod());
        } catch (IllegalArgumentException exception) {
            return null;
        }

        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);
        return handlerExecutions.get(handlerKey);
    }

    private void registerHandlers(final Class<?> controllerClass) {
        final Object controller;
        try {
            controller = controllerClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Failed to register controller: " + controllerClass.getName(), exception);
        }

        for (Method method : controllerClass.getDeclaredMethods()) {
            registerHandler(controller, method);
        }
    }

    private void registerHandler(final Object controller, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping == null) {
            return;
        }

        if (!Arrays.equals(method.getParameterTypes(),
                new Class<?>[]{HttpServletRequest.class, HttpServletResponse.class})) {
            throw new IllegalStateException("Handler must accept request and response: " + method);
        }

        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        final boolean isFallbackMapping = requestMapping.method().length == 0;
        for (RequestMethod requestMethod : resolveRequestMethods(requestMapping)) {
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            if (isFallbackMapping && handlerExecutions.containsKey(handlerKey)) {
                continue;
            }
            handlerExecutions.put(handlerKey, handlerExecution);
            log.debug("Mapped {} {} to {}", requestMethod, requestMapping.value(), method.getName());
        }
    }

    private RequestMethod[] resolveRequestMethods(final RequestMapping requestMapping) {
        final RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }
}
