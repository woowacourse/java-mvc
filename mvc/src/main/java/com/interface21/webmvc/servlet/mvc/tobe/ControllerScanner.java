package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (final Class<?> controllerType : reflections.getTypesAnnotatedWith(Controller.class)) {
            controllers.put(controllerType, createController(controllerType));
        }
        return controllers;
    }

    private Object createController(final Class<?> controllerType) {
        try {
            return controllerType.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러를 생성할 수 없습니다: " + controllerType.getName(), e);
        }
    }
}
