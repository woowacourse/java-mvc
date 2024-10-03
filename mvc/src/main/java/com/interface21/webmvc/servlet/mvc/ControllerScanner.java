package com.interface21.webmvc.servlet.mvc;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ControllerScanner {

    private final Map<Class<?>, Object> controllers;

    public ControllerScanner(Object[] basePackage) {
        this.controllers = initializeController(basePackage);
    }

    private Map<Class<?>, Object> initializeController(Object[] basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(classes);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> classes) {
        return classes.stream()
                .collect(Collectors.toMap(
                        clazz -> clazz,
                        this::constructController
                ));
    }


    private Object constructController(Class<?> annotatedClass) {
        try {
            return annotatedClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create controller instance", e);
        }
    }

    public Map<Class<?>, Object> getControllers() {
        return controllers;
    }
}
