package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ControllerScanner {

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public ControllerScanner() {
        handlerExecutions = new HashMap<>();
    }

    public void instantiateControllers(Set<Class<?>> controllerClasses) {
        for (Class<?> clazz : controllerClasses) {
            processRequestMappingMethod(clazz);
        }
    }

    private void processRequestMappingMethod(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }

            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            createHandlerExecutions(Objects.requireNonNull(annotation));
        }
    }

    private void createHandlerExecutions(RequestMapping annotation) {
        String requestUri = annotation.value();
        for (RequestMethod requestMethod : annotation.method()) {
            HandlerKey handlerKey = new HandlerKey(requestUri, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution();
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Map<HandlerKey, HandlerExecution> getHandlerExecutions() {
        return handlerExecutions;
    }
}
