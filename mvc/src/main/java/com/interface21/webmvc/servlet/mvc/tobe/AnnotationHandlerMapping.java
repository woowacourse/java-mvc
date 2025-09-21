package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.controllerScanner = new ControllerScanner(basePackages);
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        final var controllers = controllerScanner.scanAndInstantiateControllers();
        for (var entry : controllers.entrySet()) {
            registerHandlerMethods(entry.getKey(), entry.getValue());
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandlerMethods(final Class<?> controllerClass, final Object controllerInstance) {
        final var methods = controllerClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                registerHandlerMethod(controllerInstance, method);
            }
        }
    }

    private void registerHandlerMethod(final Object controllerInstance, final Method method) {
        final var annotation = method.getAnnotation(RequestMapping.class);
        final var url = annotation.value();
        final var requestMethods = annotation.method();

        if (requestMethods.length == 0) {
            for (RequestMethod requestMethod : RequestMethod.values()) {
                final var handlerKey = new HandlerKey(url, requestMethod);
                handlerExecutions.put(handlerKey, new HandlerExecution(controllerInstance, method));
            }
        } else {
            for (RequestMethod requestMethod : requestMethods) {
                final var handlerKey = new HandlerKey(url, requestMethod);
                handlerExecutions.put(handlerKey, new HandlerExecution(controllerInstance, method));
            }
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(
                new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(RequestMethod.class, request.getMethod()))
        );
    }
}
