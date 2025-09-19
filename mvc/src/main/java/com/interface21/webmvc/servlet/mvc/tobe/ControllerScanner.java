package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllerTypes = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllerTypes);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllerClasses) {
        Map<Class<?>, Object> controllerInstances = new HashMap<>();
        for (Class<?> controllerClass : controllerClasses) {
            Object controllerInstance = getInstance(controllerClass);
            controllerInstances.put(controllerClass, controllerInstance);
        }
        return controllerInstances;
    }

    private Object getInstance(Class<?> classType) {
        try {
            return classType.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}


