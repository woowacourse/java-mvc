package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> controllerClass : controllerClasses) {
            controllers.put(controllerClass, createController(controllerClass));
        }
        return controllers;
    }

    private Object createController(final Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("리플렉션 작업 중 예외 발생: " + controllerClass.getName(), e);
        }
    }
}
