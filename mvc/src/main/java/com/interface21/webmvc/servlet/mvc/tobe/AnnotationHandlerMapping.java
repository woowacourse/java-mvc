package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final Map<Class<?>, Object> handlerInstances;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
        this.handlerInstances = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controllerClass : controllerClasses) {
            registerHandlers(controllerClass);
        }
    }

    private void registerHandlers(Class<?> clazz) {
        List<Method> methods = findMethodsWithRequestMapping(clazz);
        methods.forEach(method -> addHandlerExecutions(clazz, method));
    }

    private static List<Method> findMethodsWithRequestMapping(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void addHandlerExecutions(Class<?> clazz, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        RequestMethod[] requestMethods = getRequestMethods(requestMapping);
        Arrays.stream(requestMethods).forEach(requestMethod -> {
            String uri = requestMapping.value();
            addHandlerExecution(clazz, method, uri, requestMethod);
        });
    }

    private static RequestMethod[] getRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }

    private void addHandlerExecution(Class<?> clazz, Method method, String uri, RequestMethod requestMethod) {
        HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
        Object handlerInstance = createHandlerInstance(clazz);
        HandlerExecution handlerExecution = new HandlerExecution(method, handlerInstance);
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    private Object createHandlerInstance(Class<?> clazz) {
        if (handlerInstances.containsKey(clazz)) {
            return handlerInstances.get(clazz);
        }
        try {
            handlerInstances.put(clazz, clazz.getConstructor().newInstance());
        } catch (Exception e) {
            throw new RuntimeException("Cannot create instance for class: " + clazz.getName());
        }
        return handlerInstances.get(clazz);
    }

    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod method = RequestMethod.find(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(uri, method);

        return handlerExecutions.get(handlerKey);
    }
}
