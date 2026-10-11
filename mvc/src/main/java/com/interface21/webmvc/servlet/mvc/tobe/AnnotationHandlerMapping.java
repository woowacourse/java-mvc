package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        controllerScanner.getControllers().forEach(this::registerHandlers);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandlers(final Class<?> controllerClass, final Object controller) {
        for (final Method method : controllerClass.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }
            registerHandler(controller, method);
        }
    }

    private void registerHandler(final Object controller, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        for (final RequestMethod requestMethod : resolveRequestMethods(requestMapping)) {
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            validateNotDuplicated(handlerKey, method);
            handlerExecutions.put(handlerKey, handlerExecution);
            log.info("Path : {}, Method : {}, Handler : {}", requestMapping.value(), requestMethod, method);
        }
    }

    private void validateNotDuplicated(final HandlerKey handlerKey, final Method method) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalStateException("이미 등록된 요청 매핑입니다: " + handlerKey + ", Handler : " + method);
        }
    }

    private RequestMethod[] resolveRequestMethods(final RequestMapping requestMapping) {
        final RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return findRequestMethod(request.getMethod())
                .map(requestMethod -> new HandlerKey(request.getRequestURI(), requestMethod))
                .map(handlerExecutions::get)
                .orElse(null);
    }

    private Optional<RequestMethod> findRequestMethod(final String method) {
        return Arrays.stream(RequestMethod.values())
                .filter(requestMethod -> requestMethod.name().equals(method))
                .findFirst();
    }
}
