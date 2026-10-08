package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final var controllers = new ControllerScanner(basePackage).getControllers();
        for (final var controller : controllers.entrySet()) {
            for (final var method : controller.getKey().getMethods()) {
                if (!method.isAnnotationPresent(RequestMapping.class)) {
                    continue;
                }

                final var requestMapping = method.getAnnotation(RequestMapping.class);
                final var requestMethods = requestMapping.method().length == 0
                        ? RequestMethod.values() : requestMapping.method();
                final var handlerExecution = new HandlerExecution(controller.getValue(), method);
                for (final var requestMethod : requestMethods) {
                    final var handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
                    if (handlerExecutions.putIfAbsent(handlerKey, handlerExecution) != null) {
                        throw new IllegalStateException("Duplicate request mapping: " + handlerKey);
                    }
                }
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final var handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
