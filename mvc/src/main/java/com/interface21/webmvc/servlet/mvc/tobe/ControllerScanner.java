package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
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
        final Set<Class<?>> controllerAnnotatedClasses = reflections.getTypesAnnotatedWith(Controller.class);
        final Map<Class<?>, Object> controllers = new HashMap<>();

        for (Class<?> controllerClass : controllerAnnotatedClasses) {
            controllers.put(controllerClass, createInstance(controllerClass));
        }

        return controllers;
    }

    private Object createInstance(final Class<?> clazz) {
        // 리플렉션 예외를 밖으로 안 내보내고 어떤 클래스에서 실패했는지 담아 런타임 예외로 바꾼다
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러를 생성할 수 없습니다: " + clazz.getName(), e);
        }
    }
}
