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
import java.util.stream.Stream;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final var reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        try {
            for (final Class<?> controllerClass : controllerClasses) {
                final Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
                registerControllerMethods(controllerInstance, controllerClass.getDeclaredMethods());
            }
        } catch (Exception e) {
            log.error("Error creating controller instance", e);
            throw new RuntimeException(e);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerControllerMethods(final Object controllerInstance, final java.lang.reflect.Method[] methods) {
        for (final java.lang.reflect.Method method : methods) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }

            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            final Stream<RequestMethod> requestMethods = getRequestMethods(requestMapping);

            requestMethods.forEach(requestMethod -> {
                registerHandler(controllerInstance, method, requestMethod, requestMapping);
            });
        }
    }

    private void registerHandler(Object controllerInstance, Method method, RequestMethod requestMethod,
                                 RequestMapping requestMapping) {
        final var handlerKey = new HandlerKey(requestMapping.value(), requestMethod);

        if (handlerExecutions.containsKey(handlerKey)) {
            HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);
            log.error("Duplicate mapping found. Cannot map {}. It is already mapped to {}",
                    method, handlerExecution.getMethod());
            throw new IllegalStateException("Mapping conflict: The path '" + handlerKey + "' is already in use.");
        }

        final var handlerExecution = new HandlerExecution(controllerInstance, method);
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    private Stream<RequestMethod> getRequestMethods(final RequestMapping requestMapping) {
        final RequestMethod[] methods = requestMapping.method();
        if (methods.length == 0) {
            return Arrays.stream(RequestMethod.values());
        }
        return Arrays.stream(methods);
    }

    public Object getHandler(final HttpServletRequest request) {
        final var requestUri = request.getRequestURI();
        final var requestMethod = RequestMethod.valueOf(request.getMethod());
        final var handlerKey = new HandlerKey(requestUri, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
