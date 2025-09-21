package com.interface21.webmvc.servlet.mvc.tobe;


import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getClasses() throws Exception {
        Set<Class<?>> typesWithAnnotation = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(typesWithAnnotation);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> annotatedControllers)
            throws Exception {
        final Map<Class<?>, Object> controllers = new HashMap<>();

        for (Class<?> type : annotatedControllers) {
            Object instance = type.getConstructor().newInstance();
            controllers.put(type, instance);
        }

        return controllers;
    }
}
