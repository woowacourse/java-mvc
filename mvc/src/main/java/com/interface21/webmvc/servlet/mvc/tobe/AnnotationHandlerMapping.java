package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
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
        try {
            log.info("Initialized AnnotationHandlerMapping!");
            Map<Class<?>, Object> classObjectMap = ControllerScanner.scanControllers(basePackage);
            Map<Method, Object> methods = scanRequestMappingMethod(classObjectMap);
            addMappingsForMethod(methods);
        } catch (Exception e) {
            log.error("Failed to initialize handler mappings", e);
            throw new RuntimeException("Failed to initialize handler mappings.", e);
        }
    }

    private Map<Method, Object> scanRequestMappingMethod(Map<Class<?>, Object> classObjectMap) throws Exception {
        Map<Method, Object> methods = new HashMap<>();
        for (Class<?> clazz : classObjectMap.keySet()) {
            Object object = clazz.getDeclaredConstructor().newInstance();
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    methods.put(method, object);
                }
            }
        }
        return methods;
    }

    private void addMappingsForMethod(Map<Method, Object> methods) {
        for (Map.Entry<Method, Object> entry : methods.entrySet()) {
            Method method = entry.getKey();
            Object controllerInstance = entry.getValue();
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

            for (RequestMethod requestMethod : requestMapping.method()) {
                HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
                if (handlerExecutions.containsKey(handlerKey)) {
                    throw new IllegalStateException("Duplicate mapping found: " + handlerKey);
                }

                HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
                handlerExecutions.put(handlerKey, handlerExecution);
                log.info("Mapped [{}] -> {}", handlerKey, method.getName());
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        HandlerExecution handlerExecution = handlerExecutions.getOrDefault(handlerKey, null);

        if (handlerExecution == null) {
            throw new IllegalArgumentException(
                    "No handler found for " + request.getMethod() + " " + request.getRequestURI()
            );
        }

        return handlerExecution;
    }
}
