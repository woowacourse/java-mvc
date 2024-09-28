package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Map<Class<?>, Object> controllers = new HashMap<>();

    public ControllerScanner(Object... basePackage) {
        Reflections reflections = new Reflections(basePackage);
        reflections.getTypesAnnotatedWith(Controller.class)
                .forEach(controller -> controllers.put(controller, instantiateController(controller)));
    }

    private Object instantiateController(Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<Class<?>, Object> getControllers() {
        return controllers;
    }
}
