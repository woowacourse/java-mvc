package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        final Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controller : controllers) {
            processController(controller);
        }
    }

    private void processController(Class<?> controller) {
        Object controllerInstance = getControllerInstance(controller);
        List<Method> annotatedMethods = Arrays.stream(controller.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();

        for (Method method : annotatedMethods) {
            registerHandlerExecution(controllerInstance, method);
        }
    }

    private Object getControllerInstance(Class<?> controller) {
        try {
            return controller.getDeclaredConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void registerHandlerExecution(Object controllerInstance, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        RequestMethod[] requestMethods = findRequestMethods(requestMapping);

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private RequestMethod[] findRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }

        return requestMethods;
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.of(request.getMethod()));

        if (!handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException("No handler found for request uri: " + request.getRequestURI());
        }

        return handlerExecutions.get(handlerKey);
    }
}
