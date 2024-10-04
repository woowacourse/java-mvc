package com.interface21.webmvc.servlet.mvc.annotation;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        try {
            Set<Class<?>> controllerTypes = reflections.getTypesAnnotatedWith(Controller.class);
            return instantiateControllers(controllerTypes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllerTypes) throws Exception {
        Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> controllerType : controllerTypes) {
            Object controller = controllerType.getConstructor().newInstance();
            controllers.put(controllerType, controller);
        }
        return controllers;
    }
}
