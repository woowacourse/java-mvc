package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;
    private final Map<Class<?>, Object> controllers = new HashMap<>();

    public ControllerScanner(String packages) {
        this.reflections = new Reflections(packages);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class); // 스캔
        return instantiateControllers(classes);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> classes) {
        Map<Class<?>, Object> controllers = new HashMap<>();

        for (Class<?> clazz : classes) {
            try {
                controllers.put(clazz, clazz.getDeclaredConstructor().newInstance());
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("인스턴스 생성 실패: " + clazz.getName(), e);
            }
        }

        return controllers;
    }
}
