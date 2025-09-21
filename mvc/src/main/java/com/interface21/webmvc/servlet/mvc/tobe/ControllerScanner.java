package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        return instantiateControllers(reflections.getTypesAnnotatedWith(Controller.class));
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllerClasses) {
        Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> controllerClass : controllerClasses) {
            try {
                Object instance = controllerClass.getDeclaredConstructor().newInstance();
                controllers.put(controllerClass, instance);
            } catch (Exception e) {
                throw new IllegalStateException("컨트롤러 인스턴스 생성 실패: " + controllerClass.getName(), e);
            }
        }

        return controllers;
    }
}
