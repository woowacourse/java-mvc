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

    @Override
    public void initialize() {
        controllerScanner.getControllers().forEach((controllerClass, controller) -> {
            for (Method method : controllerClass.getMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    registerHandler(controller, method);
                }
            }
        });
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandler(final Object controller, final Method method) {
        final var requestMapping = method.getAnnotation(RequestMapping.class);
        final var execution = new HandlerExecution(controller, method);
        final var requestMethods = requestMapping.method().length == 0
                ? RequestMethod.values()
                : requestMapping.method();

        for (RequestMethod requestMethod : requestMethods) {
            final var key = new HandlerKey(requestMapping.value(), requestMethod);
            final var previous = handlerExecutions.putIfAbsent(key, execution);
            if (previous != null && previous != execution) {
                throw new IllegalStateException("Duplicate handler mapping: " + key);
            }
        }
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod;
        try {
            requestMethod = RequestMethod.valueOf(request.getMethod());
        } catch (IllegalArgumentException e) {
            return null;
        }
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), requestMethod));
    }
}
