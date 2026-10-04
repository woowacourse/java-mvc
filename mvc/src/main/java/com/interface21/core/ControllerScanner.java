package com.interface21.core;

import com.interface21.context.stereotype.Controller;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Object[] basePackage;

    public ControllerScanner(final Object[] basePackage) {
        this.basePackage = basePackage;
    }

    public Map<Class<?>, Object> scan() {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return controllerClasses.stream()
                .map(this::createInstance)
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    private Entry<Class<?>, Object> createInstance(final Class<?> controllerClass) {
        try {
            return Map.entry(controllerClass, controllerClass.getConstructor().newInstance());
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러 객체를 생성할 수 없습니다: " + controllerClass.getName(), e);
        }
    }
}
