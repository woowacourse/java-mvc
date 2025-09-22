package com.interface21.webmvc.servlet.mvc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComponentScanner {

    private static final Logger log = LoggerFactory.getLogger(ComponentScanner.class);

    private final Map<Class<?>, Object> instances = new HashMap<>();
    private final Reflections reflections;

    public ComponentScanner(final Object... basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    public void scan(final Class<? extends Annotation> clazz) {
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(clazz);

        for (Class<?> type : types) {
            try {
                Constructor<?> constructor = type.getDeclaredConstructor();
                constructor.setAccessible(true);
                Object instance = constructor.newInstance();
                instances.put(type, instance);
            } catch (NoSuchMethodException e) {
                log.info("No Such Method");
            } catch (Exception e) {
                throw new IllegalStateException();
            }
        }
    }

    public Map<Class<?>, Object> getInstances() {
        return Map.copyOf(instances);
    }
}
