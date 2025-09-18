package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
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
            final var requestMappingMethods = getRequestMappingMethods(contollerClass);
            addHandlerExecutions(requestMappingMethods, controllerInstance);
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

    private Set<Method> getRequestMappingMethods(final Class<?> contollerClass) {
        final var methods = contollerClass.getDeclaredMethods();
        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private void addHandlerExecutions(final Set<Method> methods, Object controllerInstance) {
        for (final var method : methods) {
            final var handlerKeys = mapHandlerKeys(method);
            final var handlerExecution = new HandlerExecution(controllerInstance, method);
            handlerKeys.forEach(handlerKey -> handlerExecutions.put(handlerKey, handlerExecution));
        }
    }

    private Set<HandlerKey> mapHandlerKeys(final Method method) {
        final var mappedAnnotation = method.getAnnotation(RequestMapping.class);
        if (mappedAnnotation == null) {
            return Collections.emptySet();
        }

        final var url = Objects.requireNonNull(mappedAnnotation).value();
        final var requestMethods = mappedAnnotation.method();
        return Arrays.stream(requestMethods)
                .map(requestMethod -> createHandlerKey(url, requestMethod))
                .collect(Collectors.toSet());
    }

    private HandlerKey createHandlerKey(String url, RequestMethod method) {
        final var handlerKey = new HandlerKey(url, method);
        log.debug("HandlerKey : {}", handlerKey);
        return handlerKey;
    }
}
