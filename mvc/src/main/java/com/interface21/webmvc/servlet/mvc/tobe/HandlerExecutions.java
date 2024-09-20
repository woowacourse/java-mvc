package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

public class HandlerExecutions {

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public HandlerExecutions() {
        this.handlerExecutions = new HashMap<>();
    }

    public void registerController(Set<Class<?>> controllers) throws Exception {
        for (Class<?> controller : controllers) {
            Constructor<?> constructor = controller.getDeclaredConstructor();
            Object instance = constructor.newInstance();
            List<Method> methods = findRequestMappingMethods(controller);
            registerRequestMappingMethod(methods, instance);
        }
    }

    private List<Method> findRequestMappingMethods(Class<?> controller) {
        return Arrays.stream(controller.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void registerRequestMappingMethod(List<Method> methods, Object instance) {
        methods.forEach(method -> {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            registerMethod(instance, method, requestMapping);
        });
    }

    private void registerMethod(Object instance, Method method, RequestMapping requestMapping) {
        String uri = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(instance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public HandlerExecution getHandlerExecution(HandlerKey handlerKey) {
        return handlerExecutions.get(handlerKey);
    }
}
