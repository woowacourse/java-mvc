package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        ControllerScanner scanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = scanner.scan();
        controllers.forEach((clazz, object) -> Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> addHandlerExecution(object, method)));
        log.info("Initialized AnnotationHandlerMapping!");
        handlerExecutions.keySet().forEach(key -> log.info("{}", key));
    }

    private void addHandlerExecution(final Object controller, final Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String path = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(path, requestMethod);
            HandlerExecution execution = new HandlerExecution(controller, method);
            validateUnique(handlerKey);
            handlerExecutions.put(handlerKey, execution);
        }
    }

    private void validateUnique(final HandlerKey handlerKey) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException("Duplicated handlerKey: " + handlerKey);
        }
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        String method = request.getMethod();
        String requestUri = request.getRequestURI();
        HandlerKey handlerKey = new HandlerKey(requestUri, RequestMethod.from(method));
        return handlerExecutions.get(handlerKey);
    }
}
