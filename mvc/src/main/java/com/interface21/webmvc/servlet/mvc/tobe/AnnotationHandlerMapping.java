package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping {

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final var reflections = new Reflections(basePackage);
        final var controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (final var controllerClass : controllers) {
            registerController(controllerClass);
        }
    }

    private void registerController(final Class<?> controllerClass) {
        try {
            final var controller = controllerClass.getDeclaredConstructor()
                    .newInstance();

            for (final var method : controllerClass.getDeclaredMethods()) {
                registerHandlerMethods(controller, method);
            }
        } catch (final Exception e) {
            throw new IllegalStateException("Failed to instantiate controller: " + controllerClass.getName(), e);
        }
    }

    private void registerHandlerMethods(
            final Object controller,
            final Method method
    ) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            return;
        }

        final var mapping = method.getAnnotation(RequestMapping.class);
        final var url = mapping.value();
        final var requestMethods = mapping.method().length > 0 ? mapping.method() : RequestMethod.values();

        for (final var requestMethod : requestMethods) {
            registerHandler(url, requestMethod, controller, method);
        }
    }

    private void registerHandler(
            final String url,
            final RequestMethod requestMethod,
            final Object controller,
            final Method method
    ) {
        final var handlerKey = new HandlerKey(url, requestMethod);
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalStateException("Duplicate mapping: " + requestMethod + " " + url);
        }

        handlerExecutions.put(handlerKey, new HandlerExecution(controller, method));
    }

    public Object getHandler(final HttpServletRequest request) {
        final var uri = request.getRequestURI();
        final var methodName = request.getMethod();

        final var method = RequestMethod.from(methodName)
                .orElseThrow(() -> new IllegalStateException("Unsupported HTTP method: " + methodName + " for " + uri));

        final var handler = handlerExecutions.get(new HandlerKey(uri, method));
        if (handler == null) {
            throw new IllegalStateException("No handler found: " + method + " " + uri);
        }

        return handler;
    }
}
