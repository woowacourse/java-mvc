package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

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

        for (Class<?> clazz : controllerClasses) {
            Object controllerInstance = createControllerInstance(clazz);
            registerController(clazz, controllerInstance);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Object createControllerInstance(Class<?> clazz) {
        Object controllerInstance;
        try {
            controllerInstance = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return controllerInstance;
    }

    private void registerController(Class<?> clazz, Object controllerInstance) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)){
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

    public Object getHandler(final HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestPath, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
