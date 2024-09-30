package com.interface21.webmvc.servlet.mvc.mapping.impl;

import com.interface21.core.util.ReflectionUtils;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.mapping.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllerRegistry = controllerScanner.getControllerInstance();

        for (Map.Entry<Class<?>, Object> entry : controllerRegistry.entrySet()) {
            List<Method> methods = ReflectionUtils.getAllMethods(entry.getKey());

            for (Method method : methods) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

                for (RequestMethod requestMethod : requestMapping.method()) {
                    HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
                    HandlerExecution handlerExecution = new HandlerExecution(method, entry.getValue());
                    this.handlerExecutions.put(handlerKey, handlerExecution);
                }
            }
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        if (!handlerExecutions.containsKey(handlerKey)) {
            throw new AnnotationHandlerNotFoundException(request.getRequestURI());
        }
        return this.handlerExecutions.get(handlerKey);
    }

    @Override
    public boolean supports(HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return this.handlerExecutions.containsKey(handlerKey);
    }
}
