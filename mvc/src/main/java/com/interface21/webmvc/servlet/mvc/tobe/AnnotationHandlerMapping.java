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

public class AnnotationHandlerMapping implements HandlerMapping{

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
        this.controllerScanner = new ControllerScanner();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        final Set<Class<?>> controllerTypes = scanControllers();
        instantiateControllers(controllerTypes);
        registerHandlers();
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        String method = request.getMethod();
        RequestMethod requestMethod = RequestMethod.getRequestMethod(method);
        HandlerKey handlerKey = new HandlerKey(url, requestMethod);

        HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);
        return handlerExecution;
    }

    private Set<Class<?>> scanControllers() {
        final Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void instantiateControllers(Set<Class<?>> controllers) {
        controllerScanner.instantiateControllers(controllers);
    }

    private void registerHandlers() {
        final Map<Class<?>, Object> controllerInstances = controllerScanner.getControllerInstances();

        for (var controllerKey : controllerInstances.keySet()) {
            registerHandlerMethods(controllerKey.getDeclaredMethods(), controllerInstances.get(controllerKey));
        }
    }

    private void registerHandlerMethods(final Method[] methods, final Object controllerInstance) {
        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                registerHandlerMethod(controllerInstance, method);
            }
        }
    }

    private void registerHandlerMethod(Object controllerInstance, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String url = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }

        for (var requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);

            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }
}
