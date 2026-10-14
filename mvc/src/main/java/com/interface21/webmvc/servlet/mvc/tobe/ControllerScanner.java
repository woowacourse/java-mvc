package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    public Map<Class<?>, Object> scan() {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (final var controllerClass : reflections.getTypesAnnotatedWith(Controller.class)) {
            controllers.put(controllerClass, instantiate(controllerClass));
        }
        return controllers;
    }

    private Object instantiate(final Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러 생성 실패: " + controllerClass.getName(), e);
        }
    }
}
