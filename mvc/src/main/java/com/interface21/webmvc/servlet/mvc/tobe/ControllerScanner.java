package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> clazz = reflections.getTypesAnnotatedWith(Controller.class);
        return Collections.unmodifiableMap(instantiateControllers(clazz));
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllers) {
        Map<Class<?>, Object> result = new HashMap<>();
        controllers.forEach(clazz -> result.put(clazz, createInstance(clazz)));
        return result;
    }

    private Object createInstance(Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException("controller 리플렉션 중 실패");
        }
    }
}
