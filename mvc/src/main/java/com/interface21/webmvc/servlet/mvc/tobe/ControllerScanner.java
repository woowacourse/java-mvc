package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;

public class ControllerScanner {

    public Map<Class<?>, Object> scan(final Object... basePackage) {
        final Reflections reflections = new Reflections(basePackage);
        final Map<Class<?>, Object> controllers = new HashMap<>();

        for (Class<?> controller : reflections.getTypesAnnotatedWith(Controller.class)) {
            controllers.put(controller, newInstance(controller));
        }

        return controllers;
    }

    private Object newInstance(final Class<?> controller) {
        try {
            return controller.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러 인스턴스 생성 실패: " + controller.getName(), e);
        }
    }
}
