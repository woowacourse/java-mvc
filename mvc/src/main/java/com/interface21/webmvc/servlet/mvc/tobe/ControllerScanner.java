package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    ControllerScanner(final Object... basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        final Map<Class<?>, Object> controllers = instantiateControllers(classes);

        return Collections.unmodifiableMap(controllers);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> classes) {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (final Class<?> clazz : classes) {
            try {
                final Object instance = clazz.getDeclaredConstructor().newInstance();
                controllers.put(clazz, instance);
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("컨트롤러 생성 실패: " + clazz.getName(), e);
            }
        }

        return controllers;
    }


}
