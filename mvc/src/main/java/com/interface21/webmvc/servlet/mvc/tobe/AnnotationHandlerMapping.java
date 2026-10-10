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
        for (final var controller : controllerScanner.getControllers().values()) {
            registerHandlers(controller);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandlers(final Object controller) {
        for (final var method : controller.getClass().getDeclaredMethods()) {
            final var requestMapping = method.getAnnotation(RequestMapping.class);
            if (requestMapping != null) {
                registerHandler(controller, method, requestMapping);
            }
        }
    }

    private void registerHandler(final Object controller, final Method method, final RequestMapping requestMapping) {
        var requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }
        final var handlerExecution = new HandlerExecution(controller, method);
        for (final var requestMethod : requestMethods) {
            final var handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            if (handlerExecutions.containsKey(handlerKey)) {
                throw new IllegalStateException("Duplicate request mapping: " + handlerKey);
            }
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final var handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
