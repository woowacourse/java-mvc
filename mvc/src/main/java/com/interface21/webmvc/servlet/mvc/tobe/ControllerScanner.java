package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import com.interface21.context.stereotype.Controller;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(classes);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> classes) {
        Map<Class<?>, Object> controllers = new HashMap<>();
        try {
            for (Class<?> clazz : classes) {
                Object controller = clazz.getConstructor().newInstance();
                controllers.put(clazz, controller);
            }
        } catch (Exception e) {
            throw new RuntimeException("instantiateControllers error");
        }
        return controllers;
    }
}
