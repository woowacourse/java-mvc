package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        return instantiateControllers(reflections.getTypesAnnotatedWith(Controller.class));
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllers) {
        final Map<Class<?>, Object> results = new HashMap<>();

        for (Class<?> controllerClass : controllers) {
            try {
                Object controller = controllerClass.getDeclaredConstructor().newInstance();
                results.put(controllerClass, controller);
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("컨트롤러를 생성할 수 없습니다: " + controllerClass.getName(), e);
            }
        }
        return results;
    }
}
