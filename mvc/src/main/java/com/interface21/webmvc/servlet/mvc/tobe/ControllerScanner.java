package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private final Object[] basePackage;

    public ControllerScanner(final Object... basePackage) {
        this.basePackage = basePackage;
    }

    public Map<Class<?>, Object> scan()
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final Set<Class<?>> controllerTypes = new HashSet<>();
        for (Object base : basePackage) {
            final var reflections = new Reflections(base);
            controllerTypes.addAll(reflections.getTypesAnnotatedWith(Controller.class));
        }

        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> controllerType : controllerTypes) {
            final var controller = controllerType.getDeclaredConstructor().newInstance();
            controllers.put(controllerType, controller);
        }
        return controllers;
    }
}
