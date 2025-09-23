package com.interface21.webmvc.servlet.controller;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final Reflections reflections;

    public ControllerScanner(final Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        try {
            Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
            return instantiateControllers(controllerClasses);
        } catch (Exception e) {
            log.error("Failed to get controllers.", e);
            throw new RuntimeException(e);
        }
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> controllerClasses) throws Exception {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> controllerClass : controllerClasses) {
            Constructor<?> controllerConstructor = controllerClass.getDeclaredConstructor();
            controllers.put(controllerClass, controllerConstructor.newInstance());
        }
        return controllers;
    }
}
