package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.handlerExecutions = initializeHandlerExecutions(basePackage);
    }

    private Map<HandlerKey, HandlerExecution> initializeHandlerExecutions(Object[] basePackage) {
        log.info("Initialized ControllerScanner!");
        Map<HandlerKey, HandlerExecution> handlerMappings = new HashMap<>();

        try {
            Reflections reflections = new Reflections(basePackage);
            Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
            for (Class<?> controllerClass : controllerClasses) {
                handlerMappings.putAll(initializeWithController(controllerClass));
            }
        } catch (ReflectiveOperationException e) {
            throw new InternalError("Internal error: Failed to initialize handler mapping");
        }

        return handlerMappings;
    }

    private Map<HandlerKey, HandlerExecution> initializeWithController(Class<?> controllerClass)
            throws ReflectiveOperationException {
        Map<HandlerKey, HandlerExecution> handlerMappings = new HashMap<>();

        Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
        Method[] methods = controllerClass.getDeclaredMethods();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }
            handlerMappings.putAll(initializeWithRequestMapping(method, controllerInstance));
        }

        return handlerMappings;
    }

    private Map<HandlerKey, HandlerExecution> initializeWithRequestMapping(Method targetMethod,
                                                                           Object controllerClass) {
        Map<HandlerKey, HandlerExecution> handlerMappings = new HashMap<>();

        RequestMapping requestMapping = targetMethod.getAnnotation(RequestMapping.class);
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMapping.method().length == 0) {
            requestMethods = RequestMethod.values();
        }
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controllerClass, targetMethod);
            handlerMappings.put(handlerKey, handlerExecution);
        }

        return handlerMappings;
    }

    @Override
    public boolean support(HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.containsKey(handlerKey);
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
