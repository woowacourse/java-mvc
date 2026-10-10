package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this(new ControllerScanner(basePackage));
    }

    public AnnotationHandlerMapping(final ControllerScanner controllerScanner) {
        this.controllerScanner = controllerScanner;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final var controllers = controllerScanner.scan();
        for (final var entry : controllers.entrySet()) {
            final var controllerClass = entry.getKey();
            final var target = entry.getValue();
            for (final var method : findHandlerMethods(controllerClass)) {
                registerHandler(target, method);
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod;
        try {
            requestMethod = RequestMethod.valueOf(request.getMethod());
        } catch (IllegalArgumentException e) {
            return null;
        }
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), requestMethod));
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
}
