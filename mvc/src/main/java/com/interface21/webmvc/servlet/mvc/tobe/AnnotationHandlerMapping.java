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
            Reflections reflections = new Reflections(packageName);
            Set<Class<?>> controllersClasses = reflections.getTypesAnnotatedWith(Controller.class);

            for (Class<?> controllersClass : controllersClasses) {
                Method[] declaredMethods = controllersClass.getDeclaredMethods();

                for (Method declaredMethod : declaredMethods) {
                    if (declaredMethod.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping requestMapping = declaredMethod.getAnnotation(RequestMapping.class);

                        String url = requestMapping.value();
                        RequestMethod[] method = requestMapping.method();

                        HandlerKey handlerKey = new HandlerKey(url, method[0]);
                        Object controllerInstance = getInstanceBy(controllersClass);
                        HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, declaredMethod);

                        handlerExecutions.put(handlerKey, handlerExecution);
                    }
                }
            }
        }

        log.info("Initialized AnnotationHandlerMapping!");
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
        String url = request.getRequestURI();
        String method = request.getMethod();
        RequestMethod requestMethod = RequestMethod.valueOf(method);

        HandlerKey handlerKey = new HandlerKey(url, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
