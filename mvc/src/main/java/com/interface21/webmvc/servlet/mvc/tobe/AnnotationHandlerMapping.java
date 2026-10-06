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
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : controllers) {
            Object controller = createController(clazz);
            registerHandlerMethods(controller, clazz);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod method;
        try {
            method = RequestMethod.valueOf(request.getMethod());
        } catch (IllegalArgumentException e) {
            return null;
        }
        HandlerKey handlerKey = new HandlerKey(requestURI, method);
        return handlerExecutions.get(handlerKey);
    }

    private Object createController(Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    private void registerHandlerMethods(Object controller, Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }
            registerHandlerMethod(controller, method);
        }
    }

    private void registerHandlerMethod(Object controller, Method method) {
        HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        for (RequestMethod requestMethod : getRequestMethods(annotation)) {
            HandlerKey handlerKey = new HandlerKey(annotation.value(), requestMethod);
            if (handlerExecutions.containsKey(handlerKey)) {
                throw new IllegalStateException("[ERROR] 중복된 Handler Mapping입니다. : " + handlerKey);
            }
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private RequestMethod[] getRequestMethods(RequestMapping annotation) {
        RequestMethod[] requestMethods = annotation.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }
}
