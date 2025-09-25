package com.interface21.webmvc.servlet.mvc.tobe.handlermapping;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Object[] basePackages;

    public ControllerScanner(final Object... basePackages) {
        this.basePackages = basePackages;
    }

    public Map<Class<?>, Object> getControllers() {
        final var reflections = new Reflections(basePackages);
        final var handlerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        final var controllers = new HashMap<Class<?>, Object>();

        for (final Class<?> clazz : handlerClasses) {
            final var controller = generateInstance(clazz);
            controllers.put(clazz, controller);
        }
        return controllers;
    }

    private Object generateInstance(final Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("cannot access default constructor for " + clazz.getName(), e);
        }
    }
}
