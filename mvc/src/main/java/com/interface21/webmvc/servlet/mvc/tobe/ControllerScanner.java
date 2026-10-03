package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;

public class ControllerScanner {
    private final Reflections reflections;

    public ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> controllerClass : reflections.getTypesAnnotatedWith(Controller.class)) {
            Object controller = instantiateController(controllerClass);
            controllers.put(controllerClass, controller);
        }

        return controllers;
    }

    private Object instantiateController(final Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러를 생성할 수 없습니다: " + controllerClass.getName(), e);
        }
    }
}
