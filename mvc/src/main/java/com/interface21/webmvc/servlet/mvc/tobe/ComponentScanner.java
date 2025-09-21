package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ComponentScanner {

    public Map<Class<?>, Object> scan(final Class clazz, final Object[] basePackages) {
        final Map<Class<?>, Object> instances = new HashMap<>();
        Reflections reflections = new Reflections(basePackages);
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(clazz);

        for (Class<?> type : types) {
            try {
                Constructor<?> constructor = type.getDeclaredConstructor();
                Object instance = constructor.newInstance();
                instances.put(type, instance);
            } catch (NoSuchMethodException e) {
                System.out.println("No Such Method");
            } catch (Exception e) {
                throw new IllegalStateException();
            }
        }

        return instances;
    }
}
