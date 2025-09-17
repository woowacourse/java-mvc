package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
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

    private static class ControllerInfo {
        private Class<?> clazz;
        private Object object;

        public ControllerInfo(Class<?> clazz, Object object) {
            this.clazz = clazz;
            this.object = object;
        }
    }

    public void initialize() {
        try {
            log.info("Initialized AnnotationHandlerMapping!");
            Set<ControllerInfo> controllerInfos = scanControllers();
            Map<Method, Object> methods = scanRequestMappingMethod(controllerInfos);
            addMappingsForMethod(methods);
        } catch (Exception e) {
            log.error("Failed to initialize handler mappings", e);
            throw new RuntimeException("Failed to initialize handler mappings.", e);
        }
    }

    private Set<ControllerInfo> scanControllers() throws Exception {
        Set<ControllerInfo> controllerInfos = new HashSet<>();
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controller : controllers) {
            Object object = controller.getDeclaredConstructor().newInstance();
            controllerInfos.add(new ControllerInfo(controller, object));
        }
        return controllerInfos;
    }

    private Map<Method, Object> scanRequestMappingMethod(Set<ControllerInfo> controllerInfos) {
        Map<Method, Object> methods = new HashMap<>();
        for (ControllerInfo controllerInfo : controllerInfos) {
            for (Method method : controllerInfo.clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    methods.put(method, controllerInfo.object);
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
