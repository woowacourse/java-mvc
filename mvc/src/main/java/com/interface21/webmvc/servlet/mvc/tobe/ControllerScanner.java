package com.interface21.webmvc.servlet.mvc.tobe;

import static java.util.stream.Collectors.toMap;

import com.interface21.context.stereotype.Controller;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Map<Class, Object> controllerCache;

    public ControllerScanner(Object[] basePackages) {
        Reflections reflections = new Reflections(basePackages);
        this.controllerCache = reflections.getTypesAnnotatedWith(Controller.class)
                .stream()
                .collect(toMap(clazz -> clazz, this::newInstance));
    }

    public Set<Class<?>> getAll() {
        return new HashSet(controllerCache.keySet());
    }

    public Object get(Class<?> clazz) {
        return controllerCache.get(clazz);
    }

    private Object newInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception exception) {
            throw new RuntimeException(clazz + "의 인스턴스를 초기화할 수 없습니다.");
        }
    }
}
