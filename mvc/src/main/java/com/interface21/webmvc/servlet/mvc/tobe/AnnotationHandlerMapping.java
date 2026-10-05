package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutionByKey;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutionByKey = new HashMap<>();
    }

    public void initialize() {
        LOGGER.info("Initialized AnnotationHandlerMapping!");

        Reflections classpathScanner = new Reflections(basePackages);
        classpathScanner.getTypesAnnotatedWith(Controller.class)
                .forEach(this::registerController);
    }

    private void registerController(Class<?> controllerClass) {
        Object controllerInstance = createControllerInstance(controllerClass);
        Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(handlerMethod -> handlerMethod.isAnnotationPresent(RequestMapping.class))
                .forEach(handlerMethod -> registerHandlerMethod(controllerInstance, handlerMethod));
    }

    private Object createControllerInstance(Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (InstantiationException
                 | IllegalAccessException
                 | InvocationTargetException
                 | NoSuchMethodException e) {
            throw new IllegalStateException("Controller 객체 생성 실패. class: " + controllerClass.getSimpleName(), e);
        }
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

    public Object getHandler(final HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String requestUri = request.getRequestURI();
        if (contextPath != null && !contextPath.isEmpty()) {
            requestUri = requestUri.substring(contextPath.length());
        }

        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestUri, requestMethod);

        if (!handlerExecutionByKey.containsKey(handlerKey)) {
            throw new IllegalStateException("HandlerExecution가 존재하지 않음. handlerKey: " + handlerKey);
        }
        return handlerExecutionByKey.get(handlerKey);
    }
}
