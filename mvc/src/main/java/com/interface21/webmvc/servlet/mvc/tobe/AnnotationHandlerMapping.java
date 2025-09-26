package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethodFactory;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();
    private final ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(final ControllerScanner controllerScanner) {
        this.controllerScanner = controllerScanner;
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        final var controllers = controllerScanner.scan();
        for (final var controllerEntry : controllers.entrySet()) {
            final var controllerClass = controllerEntry.getKey();
            final var controllerInstance = controllerEntry.getValue();
            log.info("Controller: {}", controllerClass);
            final var requestMappingMethods = getRequestMappingMethods(controllerClass);
            addHandlerExecutions(requestMappingMethods, controllerInstance);
        }
    }

    @Override
    public Optional<Object> getHandler(final HttpServletRequest request) {
        final var handlerKey = getHandlerKey(request);
        log.debug("Target HandlerKey : {}", handlerKey);
        return Optional.ofNullable(handlerExecutions.get(handlerKey));
    }

    private Set<Method> getRequestMappingMethods(final Class<?> controllerClass) {
        final var methods = controllerClass.getMethods();
        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private void addHandlerExecutions(final Set<Method> methods, Object controllerInstance) {
        for (final var method : methods) {
            final var handlerKeys = mapHandlerKeys(method);
            final var handlerExecution = new HandlerExecution(controllerInstance, method);
            handlerKeys.forEach(handlerKey -> addHandlerExecution(handlerKey, handlerExecution));
        }
    }

    private void addHandlerExecution(HandlerKey handlerKey, HandlerExecution handlerExecution) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalStateException("Duplicate Handler Key : " + handlerKey);
        }
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    private Set<HandlerKey> mapHandlerKeys(final Method method) {
        final var mappedAnnotation = method.getAnnotation(RequestMapping.class);
        if (mappedAnnotation == null) {
            return Collections.emptySet();
        }

        final var url = mappedAnnotation.value();
        final var requestMethods = mappedAnnotation.method();
        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(url, requestMethod))
                .collect(Collectors.toSet());
    }

    private HandlerKey getHandlerKey(final HttpServletRequest request) {
        final var method = RequestMethodFactory.from(request.getMethod())
                .orElseThrow(() -> new IllegalStateException("not found Http Method" + request.getMethod()));
        return new HandlerKey(request.getRequestURI(), method);
    }
}
