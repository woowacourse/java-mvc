package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

class ControllerScanner {

    private final Map<Class<?>, Object> controllerRegistry = new HashMap<>();

    public ControllerScanner(Object[] basePackage) {
        initiateControllers(basePackage);
    }

    public Map<Class<?>, Object> getControllerInstance() {
        return Map.copyOf(controllerRegistry);
    }

    private void initiateControllers(Object[] basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> clazz : classes) {
            Constructor<?> constructor = getDeclaredConstructor(clazz);
            controllerRegistry.put(clazz, getInstance(constructor));
        }
    }

    private Constructor<?> getDeclaredConstructor(final Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException ex) {
            throw new ControllerConstructorNotFoundException(clazz, ex);
        }
    }

    private Object getInstance(final Constructor<?> constructor) {
        try {
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            throw new ControllerInstanceCreationFailedException(constructor, ex);
        }
    }
}
