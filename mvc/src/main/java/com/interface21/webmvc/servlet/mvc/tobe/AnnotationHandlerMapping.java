package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        for (final Class<?> controllerClass : findControllerClasses()) {
            final var target = createController(controllerClass);
            for (final var method : findHandlerMethods(controllerClass)) {
                registerHandler(target, method);
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final var handlerKey = new HandlerKey(
                request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }

    private Set<Class<?>> findControllerClasses() {
        return new Reflections(basePackage).getTypesAnnotatedWith(Controller.class);
    }

    private List<Method> findHandlerMethods(final Class<?> controllerClass) {
        return Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void registerHandler(final Object target, final Method method) {
        final var mapping = method.getAnnotation(RequestMapping.class);
        final var handlerExecution = new HandlerExecution(target, method);
        final var requestMethods = mapping.method().length == 0
                ? RequestMethod.values()
                : mapping.method();
        for (final var requestMethod : requestMethods) {
            final var handlerKey = new HandlerKey(mapping.value(), requestMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private Object createController(final Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to create controller: " + controllerClass.getName(), e);
        }
    }
}
