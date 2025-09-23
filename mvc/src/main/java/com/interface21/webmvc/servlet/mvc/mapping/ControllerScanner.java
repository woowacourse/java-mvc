package com.interface21.webmvc.servlet.mvc.mapping;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;


public class ControllerScanner {

    private final Object[] basePackage;

    public ControllerScanner(final Object... basePackage) {
        this.basePackage = basePackage;
    }

    public Map<Class<?>, Object> getControllers() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        Map<Class<?>, Object> controllers = new HashMap<>();

        for (Class<?> controllerClass : controllerClasses) {
            try {
                controllers.put(controllerClass, createInstance(controllerClass));
            } catch (Exception e) {
                throw new RuntimeException("Failed to create controller instance: " + controllerClass.getName(), e);
            }
        }
        return controllers;
    }

    private Object createInstance(final Class<?> clazz) throws Exception {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }
}
