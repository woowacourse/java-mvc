package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;

public class ControllerScanner {

    private final Object[] basePackages;

    public ControllerScanner(final Object... basePackages) {
        this.basePackages = basePackages;
    }

    public Map<Class<?>, Object> scanAndInstantiateControllers() {
        final var reflections = new Reflections(basePackages);
        final var controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        final Map<Class<?>, Object> controllers = new HashMap<>();

        for (var controllerClass : controllerClasses) {
            try {
                final var instance = controllerClass.getDeclaredConstructor().newInstance();
                controllers.put(controllerClass, instance);
            } catch (Exception e) {
                throw new RuntimeException("Failed to instantiate controller: " + controllerClass.getName(), e);
            }
        }
        return controllers;
    }
}
