package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Optional;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private HandlerRegistry handlerRegistry;
    private final Object[] basePackage;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.handlerRegistry = new HandlerRegistry();
        this.basePackage = basePackage.clone();
    }

    public void initialize() {
        var initializedHandlers = new HandlerRegistry();
        final var scanner = new ControllerScanner(basePackage);

        for (final var controller : scanner.scan().values()) {
            initializedHandlers = registerHandlerMethods(initializedHandlers, controller);
        }

        handlerRegistry = initializedHandlers;
        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final var requestUri = request.getRequestURI();
        final var requestMethod = findRequestMethod(request.getMethod());

        return requestMethod.map(method -> handlerRegistry.getHandler(requestUri, method)).orElse(null);
    }

    private HandlerRegistry registerHandlerMethods(final HandlerRegistry handlers, final Object controller) {
        var registeredHandlers = handlers;
        for (final var method : controller.getClass().getDeclaredMethods()) {
            final var requestMapping = method.getAnnotation(RequestMapping.class);
            if (requestMapping != null) {
                registeredHandlers = registerHandlerExecution(registeredHandlers, controller, method, requestMapping);
            }
        }
        return registeredHandlers;
    }

    private HandlerRegistry registerHandlerExecution(final HandlerRegistry handlers,
                                                     final Object controller,
                                                     final Method method,
                                                     final RequestMapping requestMapping) {
        final var execution = new HandlerExecution(controller, method);
        final var requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            return handlers.register(HandlerKey.anyMethod(requestMapping.value()), execution);
        }

        var registeredHandlers = handlers;
        for (final var requestMethod : requestMethods) {
            registeredHandlers = registeredHandlers.register(
                    new HandlerKey(requestMapping.value(), requestMethod), execution);
        }
        return registeredHandlers;
    }

    private Optional<RequestMethod> findRequestMethod(final String method) {
        if (method == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(RequestMethod.valueOf(method));
        } catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }
}
