package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        createController().forEach(this::checkMethodOfController);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        if (handlerExecutions.containsKey(handlerKey)) {
            return handlerExecutions.get(handlerKey);
        }
        throw new IllegalArgumentException("No handler found for " + request.getRequestURI() + request.getMethod());
    }

    private List<Object> createController() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return annotatedClasses.stream()
                .map(this::constructController)
                .toList();
    }

    private Object constructController(Class<?> annotatedClass) {
        try {
            return annotatedClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create controller instance", e);
        }
    }

    private void checkMethodOfController(Object controller) {
        Arrays.stream(controller.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> addHandlerExecution(controller, method));
    }

    private void addHandlerExecution(Object controller, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        Arrays.stream(requestMapping.method())
                .map(requestMethod -> new HandlerKey(requestMapping.value(), requestMethod))
                .filter(key -> !handlerExecutions.containsKey(key))
                .forEach(key -> handlerExecutions.put(key, new HandlerExecution(controller, method)));
    }
}
