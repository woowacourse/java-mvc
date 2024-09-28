package com.interface21.webmvc.servlet.mvc.annotation;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final int EMPTY = 0;

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        final ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        controllerScanner.getControllers()
                .keySet()
                .stream()
                .flatMap(clazz -> Arrays.stream(clazz.getDeclaredMethods()))
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(this::mappingHandlerExecutions);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void mappingHandlerExecutions(final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (isMethodEmpty(requestMapping)) {
            addHandlerExecutions(method, HandlerKey.buildWithAllMethodsFrom(requestMapping));
            return;
        }
        addHandlerExecutions(method, HandlerKey.buildFrom(requestMapping));
    }

    private boolean isMethodEmpty(RequestMapping requestMapping) {
        return requestMapping.method().length == EMPTY;
    }

    private void addHandlerExecutions(final Method method, final List<HandlerKey> handlerKeys) {
        for (final HandlerKey handlerKey : handlerKeys) {
            addHandlerExecution(method, handlerKey);
        }
    }

    private void addHandlerExecution(final Method method, final HandlerKey handlerKey) {
        if (handlerExecutions.containsKey(handlerKey)) {
            log.error("duplicated handlerKey: {}", handlerKey);
            throw new IllegalArgumentException("이미 존재하는 요청 매핑입니다.");
        }
        final HandlerExecution handlerExecution = HandlerExecution.from(method);
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = getHandlerKey(request);
        return handlerExecutions.computeIfAbsent(handlerKey, key -> {
            log.error("handler key: {}", key);
            throw new IllegalArgumentException("잘못된 요청입니다.");
        });
    }

    private HandlerKey getHandlerKey(final HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        return new HandlerKey(requestUri, requestMethod);
    }
}
