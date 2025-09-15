package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Object[] basePackages;

    public ControllerScanner(final Object... basePackages) {
        this.basePackages = basePackages;
    }

    public Map<Class<?>, Object> getControllers() throws Exception {
        final var reflections = new Reflections(basePackages);
        final var handlerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        final var controllers = new HashMap<Class<?>, Object>();

        for (final var clazz : handlerClasses) {
            final Object controller = clazz.getDeclaredConstructor().newInstance();
            controllers.put(clazz, controller);
        }
        return controllers;
    }
}
