package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
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
        controllerScanner.instantiateControllers(controllerScanner.getControllers())
                .values()
                .forEach(this::registerHandlers);

        log.info("Initialized AnnotationHandlerMapping with {} handlers", handlerExecutions.size());
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod;
        try {
            requestMethod = RequestMethod.valueOf(request.getMethod());
        } catch (IllegalArgumentException exception) {
            return null;
        }

        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);
        return handlerExecutions.get(handlerKey);
    }

    private void registerHandlers(final Object controller) {
        for (Method method : controller.getClass().getDeclaredMethods()) {
            registerHandler(controller, method);
        }
    }

    private void registerHandler(final Object controller, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping == null) {
            return;
        }

        if (!Arrays.equals(method.getParameterTypes(),
                new Class<?>[]{HttpServletRequest.class, HttpServletResponse.class})) {
            throw new IllegalStateException("Handler must accept request and response: " + method);
        }

        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        final boolean isFallbackMapping = requestMapping.method().length == 0;
        for (RequestMethod requestMethod : resolveRequestMethods(requestMapping)) {
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            if (isFallbackMapping && handlerExecutions.containsKey(handlerKey)) {
                continue;
            }
            handlerExecutions.put(handlerKey, handlerExecution);
            log.debug("Mapped {} {} to {}", requestMethod, requestMapping.value(), method.getName());
        }
    }

    private RequestMethod[] resolveRequestMethods(final RequestMapping requestMapping) {
        final RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }
}
