package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
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
        Set<Class<?>> annotatedController = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateController(annotatedController);
    }

    private Map<Class<?>, Object> instantiateController(Set<Class<?>> clazz) {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> controller : clazz) {
            try {
                controllers.put(controller, ReflectionUtils.accessibleConstructor(controller).newInstance());
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("컨트롤러 인스턴스 생성 실패: " + controller.getName(), e);
            }
        }
        return controllers;
    }
}
