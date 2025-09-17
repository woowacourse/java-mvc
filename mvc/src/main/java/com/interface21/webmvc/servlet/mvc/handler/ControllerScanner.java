package com.interface21.webmvc.servlet.mvc.handler;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        try {
            final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);

            return initiateControllers(classes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Map<Class<?>, Object> initiateControllers(final Set<Class<?>> classes) throws Exception {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> clazz : classes) {
            final Constructor<?> constructor = ReflectionUtils.accessibleConstructor(clazz);
            final Object instance = constructor.newInstance();
            controllers.put(clazz, instance);
        }

        return controllers;
    }
}
