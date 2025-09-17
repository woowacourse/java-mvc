package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
            scanControllers(packageName);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void scanControllers(final Object packageName) {
        final Reflections reflections = new Reflections(packageName);
        final Set<Class<?>> controllersClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controllerClass : controllersClasses) {
            registerControllerMethods(controllerClass);
        }
    }

    private void registerControllerMethods(final Class<?> controllerClass) {
        final Method[] declaredMethods = controllerClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            registerHandlerMethod(controllerClass, declaredMethod);
        }
    }

    private void registerHandlerMethod(final Class<?> controllerClass, final Method declaredMethod) {
        if (isAnnotationPresent(declaredMethod)) {
            final RequestMapping requestMapping = declaredMethod.getAnnotation(RequestMapping.class);
            final HandlerKey handlerKey = getHandlerKey(requestMapping);
            final HandlerExecution handlerExecution = getHandlerExecution(controllerClass, declaredMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private boolean isAnnotationPresent(final Method declaredMethod) {
        return declaredMethod.isAnnotationPresent(RequestMapping.class);
    }

    private HandlerKey getHandlerKey(final RequestMapping requestMapping) {
        final String url = requestMapping.value();
        final RequestMethod[] method = requestMapping.method();
        return new HandlerKey(url, method[0]);
    }

    private HandlerExecution getHandlerExecution(final Class<?> controllersClass, final Method declaredMethod) {
        final Object controllerInstance = getInstanceBy(controllersClass);
        return new HandlerExecution(controllerInstance, declaredMethod);
    }

    private Object getInstanceBy(final Class<?> controllersClass) {
        try {
            return controllersClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String url = request.getRequestURI();
        final String method = request.getMethod();
        final RequestMethod requestMethod = RequestMethod.valueOf(method);

        final HandlerKey handlerKey = new HandlerKey(url, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
