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
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : controllerClasses) {
            Object controllerInstance = createController(controllerClass);
            registerHandlerExecutions(controllerClass.getDeclaredMethods(), controllerInstance);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(
                new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }

    private Object createController(final Class<?> controllerClass) {
        Object controllerInstance;
        try {
            controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return controllerInstance;
    }

    private void registerHandlerExecutions(final Method[] methods, final Object controllerInstance) {
        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                for (RequestMethod requestMethod : mapping.method()) {
                    handlerExecutions.put(
                            new HandlerKey(mapping.value(), requestMethod),
                            new HandlerExecution(controllerInstance, method)
                    );
                }
            }
        }
    }
}
