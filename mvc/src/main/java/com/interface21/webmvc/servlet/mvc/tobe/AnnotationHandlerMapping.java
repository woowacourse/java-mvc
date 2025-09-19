package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        for (Object packageName : basePackage) {
            final Reflections reflections = new Reflections(packageName);
            final ControllerScanner controllerScanner = new ControllerScanner(reflections);
            final Map<Class<?>, Object> controllersClasses = controllerScanner.getControllers();
            for (Object controllerInstance : controllersClasses.values()) {
                registerControllerMethods(controllerInstance);
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        final String url = request.getRequestURI();
        final String method = request.getMethod();
        final RequestMethod requestMethod = RequestMethod.valueOf(method);

        final HandlerKey handlerKey = new HandlerKey(url, requestMethod);
        return handlerExecutions.get(handlerKey);
    }

    private void registerControllerMethods(final Object controllerInstance) {
        final Method[] declaredMethods = controllerInstance.getClass().getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            registerHandlerMethod(controllerInstance, declaredMethod);
        }
    }

    private void registerHandlerMethod(final Object controllerInstance, final Method declaredMethod) {
        if (isAnnotationPresent(declaredMethod)) {
            final RequestMapping requestMapping = declaredMethod.getAnnotation(RequestMapping.class);
            final List<HandlerKey> handlerKey = getHandlerKey(requestMapping);
            final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, declaredMethod);
            for (HandlerKey key : handlerKey) {
                handlerExecutions.put(key, handlerExecution);
            }
        }
    }

    private boolean isAnnotationPresent(final Method declaredMethod) {
        return declaredMethod.isAnnotationPresent(RequestMapping.class);
    }

    private List<HandlerKey> getHandlerKey(final RequestMapping requestMapping) {
        final String url = requestMapping.value();
        final RequestMethod[] method = requestMapping.method();
        if (isEmptyHttpMethod(method)) {
            return getHandlerKeysForAllHttpMethod(url);
        }

        return Arrays.stream(method)
                .map(requestMethod -> new HandlerKey(url, requestMethod))
                .toList();
    }

    private boolean isEmptyHttpMethod(final RequestMethod[] method) {
        return method.length == 0;
    }

    private List<HandlerKey> getHandlerKeysForAllHttpMethod(final String url) {
        return Arrays.stream(RequestMethod.values())
                .map(requestMethod -> new HandlerKey(url, requestMethod))
                .toList();
    }

}
