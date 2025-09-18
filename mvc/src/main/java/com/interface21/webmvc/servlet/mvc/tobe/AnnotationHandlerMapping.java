package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(final ControllerScanner controllerScanner) {
        this.controllerScanner = controllerScanner;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        final var controllers = controllerScanner.getControllers();
        for (final var controllerEntry : controllers.entrySet()) {
            final var contollerClass = controllerEntry.getKey();
            final var controllerInstance = controllerEntry.getValue();
            log.debug("Controller: {}", contollerClass);
            final var methods = contollerClass.getDeclaredMethods();
            for (final var method : methods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    putHandlerExecutions(method, controllerInstance);
                }
            }
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final var method = RequestMethod.from(request.getMethod())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 http 메서드입니다: " + request.getMethod()));
        final var handlerKey = new HandlerKey(request.getRequestURI(), method);
        log.debug("Target HandlerKey : {}", handlerKey);
        return handlerExecutions.get(handlerKey);
    }


    private void putHandlerExecutions(final Method method, final Object controllerInstance) {
        final var mapping = method.getAnnotation(RequestMapping.class);
        final var value = Objects.requireNonNull(mapping).value();
        final var requestMethods = mapping.method();

        for (final var requestMethod : requestMethods) {
            final var handlerKey = new HandlerKey(value, requestMethod);
            log.debug("HandlerKey : {}", handlerKey);
            final var handlerExecution = new HandlerExecution(controllerInstance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }
}
