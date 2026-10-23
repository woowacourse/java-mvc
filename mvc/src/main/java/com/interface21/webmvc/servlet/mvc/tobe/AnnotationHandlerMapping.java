package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.controllerScanner = new ControllerScanner(basePackage);
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final var controllers = controllerScanner.scan();
        final Map<HandlerKey, HandlerExecution> newHandlerExecutions = new HashMap<>();
        for (Map.Entry<Class<?>, Object> entry : controllers.entrySet()) {
            final var controllerType = entry.getKey();
            final var controller = entry.getValue();

            final var methods = controllerType.getDeclaredMethods();
            for (Method method : methods) {
                if (!method.isAnnotationPresent(RequestMapping.class)) {
                    continue;
                }
                final var handlerExecution = new HandlerExecution(controller, method);
                final var requestMapping = method.getDeclaredAnnotation(RequestMapping.class);

                RequestMethod[] supportedMethods = requestMapping.method();
                if (supportedMethods.length == 0) {
                    supportedMethods = RequestMethod.values();
                }

                for (RequestMethod supportedMethod : supportedMethods) {
                    final var handlerKey = new HandlerKey(requestMapping.value(), supportedMethod);
                    final var previous = newHandlerExecutions.putIfAbsent(handlerKey, handlerExecution);
                    if (previous != null && previous != handlerExecution) {
                        throw new IllegalStateException("Duplicate handler mapping for " + handlerKey
                                + ": " + previous + " and " + handlerExecution);
                    }
                    log.info("Request {} {} -> Mapped to {}#{} on instance={}",
                            supportedMethod, requestMapping.value(),
                            controllerType.getSimpleName(), method.getName(), handlerExecution);
                }
            }
        }
        handlerExecutions.clear();
        handlerExecutions.putAll(newHandlerExecutions);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod;
        try {
            requestMethod = RequestMethod.valueOf(request.getMethod());
        } catch (IllegalArgumentException e) {
            return null;
        }

        final var handlerExecution = handlerExecutions.get(new HandlerKey(request.getRequestURI(), requestMethod));
        log.info("Find handler for {} {}", request.getMethod(), request.getRequestURI());
        log.info("Found handler={}", handlerExecution);
        return handlerExecution;
    }
}
