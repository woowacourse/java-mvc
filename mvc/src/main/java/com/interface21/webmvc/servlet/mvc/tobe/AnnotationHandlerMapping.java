package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping{

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        for (Class<?> clazz : controllers.keySet()) {
            List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .toList();
            methods.forEach(method -> addHandlerExecutions(controllers.get(clazz), method));
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlerExecutions(Object handler, Method method) {
        Annotation annotation = method.getAnnotation(RequestMapping.class);
        String uri = (String) getRequestMappingMethods(annotation, "value");
        RequestMethod[] requestMethods = (RequestMethod[]) getRequestMappingMethods(annotation, "method");
        HandlerExecution handlerExecution = new HandlerExecution(handler, method);
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private static Object getRequestMappingMethods(Annotation annotation, String method) {
        try {
            return annotation.getClass()
                    .getMethod(method)
                    .invoke(annotation);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Reflect Annotation Error");
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        if(request.getMethod() == null) {
            return null;
        }
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
