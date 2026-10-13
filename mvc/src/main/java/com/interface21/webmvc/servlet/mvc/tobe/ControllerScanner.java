package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackages) {
        this.reflections = new  Reflections(basePackages);
    }

    public Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        final Map<Class<?>, Object> controllers = new HashMap<>();

        for (Class<?> controllerClass : classes) {
           final Object controller = createInstance(controllerClass);
           controllers.put(controllerClass, controller);
        }
        return controllers;
    }

    private Object createInstance(final Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("컨트롤러 인스턴스 생성 실패: " + controllerClass.getName(), e);
        }
    }
}
