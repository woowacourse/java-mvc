package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
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
        controllerScanner.scan().values()
                .forEach(this::registerHandlerExecutions);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandlerExecutions(final Object controller) {
        for (Method method : controller.getClass().getDeclaredMethods()) {
            final var requestMapping = method.getAnnotation(RequestMapping.class);
            if (requestMapping == null) {
                continue;
            }

            final var handlerExecution = new HandlerExecution(controller, method);
            for (var requestMethod : resolveRequestMethods(requestMapping)) {
                final var handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
                final var existingHandler = handlerExecutions.putIfAbsent(handlerKey, handlerExecution);
                if (existingHandler != null) {
                    throw new IllegalStateException("Duplicate handler mapping: " + handlerKey);
                }
                log.debug("Mapped {} onto {}", handlerKey, method);
            }
        }
    }

    private RequestMethod[] resolveRequestMethods(final RequestMapping requestMapping) {
        if (requestMapping.method().length == 0) {
            return RequestMethod.values();
        }
        return requestMapping.method();
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final var requestMethod = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), requestMethod));
    }
}
