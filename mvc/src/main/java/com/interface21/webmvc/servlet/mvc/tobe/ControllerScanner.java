package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> clazz : reflections.getTypesAnnotatedWith(Controller.class)) {
            controllers.put(clazz, instantiateControllers(clazz));
        }
        return controllers;
    }

    private Object instantiateControllers(final Class<?> clazz) {
        try {
            return ReflectionUtils.accessibleConstructor(clazz).newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Can't create controller: " + clazz.getName(), e);
        }
    }
}
