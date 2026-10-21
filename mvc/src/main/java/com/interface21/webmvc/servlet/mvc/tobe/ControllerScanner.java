package com.interface21.webmvc.servlet.mvc.tobe;

import org.reflections.Reflections;
import com.interface21.context.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    public Map<Class<?>, Object> createControllerInstances(final String basePackageName) {
        final Map<Class<?>, Object> instances = new HashMap<>();
        for (Class<?> controller : scanController(basePackageName)) {
            final Object instance = createInstance(controller);
            instances.put(controller, instance);
        }
        return instances;
    }

    private Set<Class<?>> scanController(final String basePackageName) {
        final Reflections reflections = new Reflections(basePackageName);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private Object createInstance(final Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(
                    clazz.getName() + " 인스턴스 생성 실패", e
            );
        }
    }
}
