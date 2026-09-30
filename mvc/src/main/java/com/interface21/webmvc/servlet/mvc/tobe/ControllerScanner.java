package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateController(controllers);
    }

    private Map<Class<?>, Object> instantiateController(Set<Class<?>> clazz) {
        Map<Class<?>, Object> controllers = new HashMap<>();
        try {
            for (Class<?> c : clazz) {
                Object controllerInstance = c.getDeclaredConstructor().newInstance();
                controllers.put(c, controllerInstance);
            }
        } catch (Exception e) {
            throw new RuntimeException("컨트롤러 인스턴스 생성에 실패했습니다.", e);
        }
        return controllers;
    }
}
