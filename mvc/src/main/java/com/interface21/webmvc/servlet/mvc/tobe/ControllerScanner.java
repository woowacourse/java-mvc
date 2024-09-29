package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;
    private final Map<Class<?>, Object> controllers;

    public ControllerScanner(Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        controllers = instantiateControllers(controllerClasses);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllerClasses) {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> controllerClass : controllerClasses) {
            controllers.put(controllerClass, getInstance(controllerClass));
        }
        return controllers;
    }

    private Object getInstance(Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (Throwable e) {
            throw new IllegalStateException("Failed to instantiate " + controllerClass.getName());
        }
    }

    public Map<Class<?>, Object> getControllers() {
        return controllers;
    }
}
