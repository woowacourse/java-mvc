package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public final class ControllerScanner {

    public static Map<Class<?>, Object> scan(final Object[] packages) {
        final Set<Class<?>> controllerClasses = getControllerClasses(packages);

        return instantiate(controllerClasses);
    }

    private static Set<Class<?>> getControllerClasses(final Object[] packages) {
        final Reflections reflections = new Reflections(packages);

        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private static Map<Class<?>, Object> instantiate(final Set<Class<?>> controllerClasses) {
        final Map<Class<?>, Object> controllers = new HashMap<>();

        for (final Class<?> clazz : controllerClasses) {
            Object controller;
            try {
                controller = clazz.getDeclaredConstructor().newInstance();
            } catch (InstantiationException e) {
                throw new IllegalStateException("This class cannot be instantiated.", e);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Failed to instantiate: target constructor is inaccessible.", e);
            } catch (InvocationTargetException e) {
                throw new IllegalStateException("An unexpected exception occurred during instance creation.", e);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("No matching constructor found.", e);
            }

            controllers.put(clazz, controller);
        }

        return controllers;
    }
}
