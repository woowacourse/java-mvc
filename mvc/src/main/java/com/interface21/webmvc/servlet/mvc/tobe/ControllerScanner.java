package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Object[] basePackage;

    public ControllerScanner(final Object... basePackage) {
        this.basePackage = basePackage;
    }

    public Map<Class<?>, Object> scan() {
        Reflections samples = new Reflections(basePackage);
        Set<Class<?>> typesAnnotatedWith = samples.getTypesAnnotatedWith(Controller.class);
        Map<Class<?>, Object> result = new HashMap<>();
        for (Class<?> controllerClass : typesAnnotatedWith) {
            Object controller = createController(controllerClass);
            result.put(controllerClass, controller);
        }
        return result;
    }

    private static Object createController(Class<?> aClass) {
        Object controller;
        try {
            controller = aClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러 생성 실패: " + aClass.getName(), e);
        }
        return controller;
    }
}
