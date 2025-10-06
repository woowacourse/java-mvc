package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Map<HandlerKey, MethodHandler>> getController() {
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(classes);
    }

    private Map<Class<?>, Map<HandlerKey, MethodHandler>> instantiateControllers(Set<Class<?>> controllers) {
        Map<Class<?>, Map<HandlerKey, MethodHandler>> controllerMappings = new HashMap<>();
        for (Class<?> controller : controllers) {
            controllerMappings.put(controller, extractHandlerExecutions(controller));
        }
        return controllerMappings;
    }

    private Map<HandlerKey, MethodHandler> extractHandlerExecutions(Class<?> controllerClass) {
        Object instance = instantiateController(controllerClass);

        Map<HandlerKey, MethodHandler> executions = new HashMap<>();
        for (Method method : controllerClass.getMethods()) {
            registerHandlerMethodIfAnnotated(method, instance, executions);
        }
        return executions;
    }

    private Object instantiateController(Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(controllerClass.getName());
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
}
