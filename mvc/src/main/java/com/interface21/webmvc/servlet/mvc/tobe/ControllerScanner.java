package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
import java.lang.reflect.Constructor;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        return classes.stream()
                .map(controller -> {
                    try {
                        Constructor<?> constructor = ReflectionUtils.accessibleConstructor(controller);
                        Object instance = constructor.newInstance();
                        return new SimpleEntry<Class<?>, Object>(controller, instance);
                    } catch (Exception e) {
                        throw new IllegalStateException(
                                "Failed to create controller instance: %s".formatted(controller.getName()), e);
                    }
                })
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }
}
