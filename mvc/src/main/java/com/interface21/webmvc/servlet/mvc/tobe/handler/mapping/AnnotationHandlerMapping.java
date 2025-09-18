package com.interface21.webmvc.servlet.mvc.tobe.handler.mapping;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        var controllerScanner = new ControllerScanner(basePackage);
        var controllers = controllerScanner.getControllers();
        for (Entry<Class<?>, Object> controller : controllers.entrySet()) {
            var methods = scanRequestMappingMethods(controller.getKey());
            registerHandlerForController(controller.getValue(), methods);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        final var requestURI = request.getRequestURI();
        final var method = request.getMethod();
        final var handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }

    private List<Method> scanRequestMappingMethods(final Class<?> controller) {
        return Arrays.stream(controller.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void registerHandlerForController(final Object controller, final List<Method> methods) {
        for (final Method method : methods) {
            final var handlerKeys = createHandlerKeys(method);
            final var handlerExecution = new HandlerExecution(controller, method);
            registerHandler(handlerKeys, handlerExecution);
        }
    }

    private List<HandlerKey> createHandlerKeys(final Method method) {
        final var requestMappingAnnotation = method.getAnnotation(RequestMapping.class);
        var url = requestMappingAnnotation.value();
        var httpMethods = requestMappingAnnotation.method();

        if (httpMethods.length == 0) {
            return Arrays.stream(RequestMethod.values())
                    .map(requestMethod -> new HandlerKey(url, requestMethod))
                    .toList();
        }
        return Arrays.stream(httpMethods)
                .map(requestMethod -> new HandlerKey(url, requestMethod))
                .toList();
    }

    private void registerHandler(final List<HandlerKey> handlerKeys, final HandlerExecution handlerExecution) {
        for (final HandlerKey handlerKey : handlerKeys) {
            registerHandler(handlerKey, handlerExecution);
        }
    }

    private void registerHandler(final HandlerKey handlerKey, final HandlerExecution handlerExecution) {
        if (handlerExecutions.containsKey(handlerKey)) {
            log.warn("Handler Key : {} 가 이미 등록되어 있습니다. | {}", handlerKey, handlerExecutions.get(handlerKey));
            throw new IllegalArgumentException();
        }
        handlerExecutions.put(handlerKey, handlerExecution);
    }
}
