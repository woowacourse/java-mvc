package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Map<Class<?>, Object> controllerInstances = ControllerScanner.createAllControllerInstances();
        for (Class<?> controllerClass : controllerInstances.keySet()) {
            Set<Method> requestMappingMethods = ReflectionUtils.getAllMethods(controllerClass, ReflectionUtils.withAnnotation(RequestMapping.class));
            for (Method requestMappingMethod : requestMappingMethods) {
                RequestMapping requestMappingAnnotation = requestMappingMethod.getAnnotation(RequestMapping.class);
                RequestMethod[] requestMappingHttpMethods = requestMappingAnnotation.method();
                for (RequestMethod requestMappingHttpMethod : requestMappingHttpMethods) {
                    HandlerKey handlerKey = new HandlerKey(requestMappingAnnotation.value(), requestMappingHttpMethod);
                    HandlerExecution handlerExecution = new HandlerExecution(controllerClass, requestMappingMethod);
                    handlerExecutions.put(handlerKey, handlerExecution);
                }
            }
        }
    }

    @Override
    public Object getHandler(HttpServletRequest httpServletRequest) {
        HandlerKey handlerKey = new HandlerKey(httpServletRequest.getRequestURI(), RequestMethod.from(httpServletRequest.getMethod()));
        return this.handlerExecutions.get(handlerKey);
    }
}
