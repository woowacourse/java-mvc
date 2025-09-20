package com.interface21.webmvc.servlet.handler.mapping;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.handler.HandlerExecution;
import com.interface21.webmvc.servlet.handler.HandlerKey;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.reflections.ReflectionUtils;

public class AnnotationHandlerMapping implements HandlerMapping {

    private final String basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final String basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Map<Class<?>, Object> controllerInstances = ControllerScanner.createAllControllerInstances(basePackage);
        for (Class<?> controllerClass : controllerInstances.keySet()) {
            registerController(controllerClass, controllerInstances.get(controllerClass));
        }
    }

    private void registerController(Class<?> controllerClass, Object controllerInstance) {
        Set<Method> requestMappingMethods = ReflectionUtils.getAllMethods(controllerClass, ReflectionUtils.withAnnotation(RequestMapping.class));
        for (Method requestMappingMethod : requestMappingMethods) {
            registerRequestMappingMethod(requestMappingMethod, controllerInstance);
        }
    }

    private void registerRequestMappingMethod(Method requestMappingMethod, Object controllerInstance) {
        RequestMapping requestMappingAnnotation = requestMappingMethod.getAnnotation(RequestMapping.class);
        for (RequestMethod requestMappingHttpMethod : requestMappingAnnotation.method()) {
            HandlerKey handlerKey = new HandlerKey(requestMappingAnnotation.value(), requestMappingHttpMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, requestMappingMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public Optional<Object> getHandler(HttpServletRequest httpServletRequest) {
        String requestURI = parseUriFromHttpServletRequest(httpServletRequest);
        HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.from(httpServletRequest.getMethod()));
        return Optional.ofNullable(this.handlerExecutions.get(handlerKey));
    }

    private String parseUriFromHttpServletRequest(HttpServletRequest httpServletRequest) {
        String requestURI = httpServletRequest.getRequestURI();
        String contextPath = httpServletRequest.getContextPath();
        if (contextPath != null) {
            return requestURI.substring(httpServletRequest.getContextPath().length());
        }
        return requestURI;
    }
}
