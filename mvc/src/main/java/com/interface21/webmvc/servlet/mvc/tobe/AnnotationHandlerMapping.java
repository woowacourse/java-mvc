package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutionByKey;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutionByKey = new HashMap<>();
    }

    public void initialize() {
        LOGGER.info("Initialized AnnotationHandlerMapping!");

        new ControllerScanner(basePackages)
                .getControllers()
                .forEach(this::registerController);
    }

    private void registerController(Class<?> controllerClass, Object controllerInstance) {
        Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(handlerMethod -> handlerMethod.isAnnotationPresent(RequestMapping.class))
                .forEach(handlerMethod -> registerHandlerMethod(controllerInstance, handlerMethod));
    }

    private void registerHandlerMethod(Object controllerInstance, Method handlerMethod) {
        handlerMethod.setAccessible(true);
        RequestMapping requestMapping = handlerMethod.getAnnotation(RequestMapping.class);
        String path = requestMapping.value();
        HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, handlerMethod);

        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }

        Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(path, requestMethod))
                .forEach(handlerKey -> registerHandlerExecution(handlerKey, handlerExecution));
    }

    private void registerHandlerExecution(
            HandlerKey handlerKey,
            HandlerExecution handlerExecution
    ) {
        if (handlerExecutionByKey.containsKey(handlerKey)) {
            throw new IllegalStateException("중복된 HandlerExecution. handlerKey: " + handlerKey);
        }
        handlerExecutionByKey.put(handlerKey, handlerExecution);
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String requestUri = request.getRequestURI();
        if (contextPath != null && !contextPath.isEmpty() && requestUri.startsWith(contextPath)) {
            requestUri = requestUri.substring(contextPath.length());
        }

        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestUri, requestMethod);
        return handlerExecutionByKey.get(handlerKey);
    }
}
