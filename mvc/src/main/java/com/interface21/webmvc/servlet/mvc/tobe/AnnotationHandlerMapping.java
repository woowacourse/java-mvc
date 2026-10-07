package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
        log.info("Initialized AnnotationHandlerMapping!");
        controllerScanner.scan().forEach(this::registerController);
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        RequestMethod requestMethod = RequestMethod.valueOf(method);
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }

    private void registerController(final Class<?> controllerClass, final Object controller) {
        for (Method method : controllerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                registerHandlerMethod(controller, method);
            }
        }
    }

    private void registerHandlerMethod(final Object controller, final Method method) {
        validateHandlerMethod(method);
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);

        for (RequestMethod requestMethod : getRequestMethods(requestMapping)) {
            addHandlerExecution(handlerExecution, requestMethod, requestMapping.value());
        }
    }

    private void validateHandlerMethod(final Method method) {
        if (!Modifier.isPublic(method.getModifiers())) {
            throw new IllegalStateException("@RequestMapping method must be public: " + method);
        }
    }

    private static RequestMethod[] getRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] methods = requestMapping.method();
        if (methods.length == 0) {
            methods = RequestMethod.values();
        }
        return methods;
    }

    private void addHandlerExecution(final HandlerExecution handlerExecution, final RequestMethod method, final String url) {
        final HandlerKey handlerKey = new HandlerKey(url, method);
        final HandlerExecution previousHandler = handlerExecutions.putIfAbsent(handlerKey, handlerExecution);

        if (previousHandler != null) {
            throw new IllegalStateException("Duplicate handler mapping: " + handlerKey);
        }
    }
}
