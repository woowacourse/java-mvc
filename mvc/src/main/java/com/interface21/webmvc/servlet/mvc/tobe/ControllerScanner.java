package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    public Map<Class<?>, Object> scan(final Object... basePackages)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Set<Class<?>> classes = new HashSet<>();
        for (final var basePackage : basePackages) {
            Reflections reflections = new Reflections(basePackage);
            classes.addAll(reflections.getTypesAnnotatedWith(Controller.class));
        }

        Map<Class<?>, Object> controllers = new HashMap<>();
        for (final var clazz : classes) {
            Object controller = clazz.getDeclaredConstructor().newInstance();
            controllers.put(clazz, controller);
        }

        return controllers;
    }
}
