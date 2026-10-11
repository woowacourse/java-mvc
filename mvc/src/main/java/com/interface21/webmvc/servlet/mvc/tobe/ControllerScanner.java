package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        final Map<Class<?>, Object> controllers = new HashMap<>();

        for (var clazz : reflections.getTypesAnnotatedWith(Controller.class)) {
            try {
                controllers.put(clazz, clazz.getDeclaredConstructor().newInstance());
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("컨트롤러 생성 실패: " + clazz.getName(), e);
            }
        }

        return controllers;
    }
}
