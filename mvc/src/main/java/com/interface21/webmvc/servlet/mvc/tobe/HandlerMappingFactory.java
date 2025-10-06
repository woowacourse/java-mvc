package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HandlerMappingFactory {
    public Map<HandlerKey, MethodHandler> createHandlerMappings(Set<Class<?>> controllerClasses) {
        Map<HandlerKey, MethodHandler> handlerMappings = new HashMap<>();
        
        for (Class<?> controllerClass : controllerClasses) {
            Object controller = instantiateController(controllerClass);
            extractHandlerMethods(controller, controllerClass, handlerMappings);
        }
        
        return handlerMappings;
    }

    private void extractHandlerMethods(Object controller, Class<?> controllerClass, 
                                     Map<HandlerKey, MethodHandler> mappings) {
        for (Method method : controllerClass.getMethods()) {
            registerHandlerMethodIfAnnotated(method, controller, mappings);
        }
    }

    private void registerHandlerMethodIfAnnotated(Method method, Object instance,
                                                Map<HandlerKey, MethodHandler> map) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            return;
        }
        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        for (RequestMethod httpMethod : mapping.method()) {
            map.put(new HandlerKey(mapping.value(), httpMethod),
                    new MethodHandler(instance, method));
        }
    }

    private Object instantiateController(Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(controllerClass.getName());
        }
    }
}
