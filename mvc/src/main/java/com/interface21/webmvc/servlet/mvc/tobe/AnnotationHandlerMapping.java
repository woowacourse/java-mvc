package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        for (var controller : reflections.getTypesAnnotatedWith(Controller.class)) {
            putController(controller);
        }
    }

    private void putController(Class<?> controllerClass) {
        List<Method> handlerMethods = Arrays.stream(controllerClass.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
        for (Method method : handlerMethods) {
            putHandlerExecution(controllerClass, method);
        }
    }

    private void putHandlerExecution(Class<?> controllerClass, Method handlerMethod) {
        RequestMapping requestMapping = handlerMethod.getAnnotation(RequestMapping.class);
        if (requestMapping.method().length == 0) {
            mappingTo(controllerClass, handlerMethod, requestMapping.value(), RequestMethod.values());
            return;
        }

        mappingTo(controllerClass, handlerMethod, requestMapping.value(), requestMapping.method());
    }

    private void mappingTo(Class<?> controllerClass, Method method, String value, RequestMethod... requestMethods) {
        for (RequestMethod requestMethod : requestMethods) {
            handlerExecutions.put(new HandlerKey(value, requestMethod), new HandlerExecution(controllerClass, method));
            log.info("map method {}() to {} {}", method.getName(), requestMethod.name(), value);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);
        if (handlerExecution == null) {
            throw new NoMatchedHandlerException(request);
        }
        return handlerExecution;
    }
}
