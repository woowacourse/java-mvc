package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public static ControllerScanner of(Object[] basePackage) {
        return new ControllerScanner(new Reflections(basePackage));
    }

    public ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> typesAnnotatedWithController = reflections.getTypesAnnotatedWith(Controller.class);

        return instantiateControllers(typesAnnotatedWithController);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> classes) {
        Map<Class<?>, Object> controllers = new HashMap<>();

        for (Class<?> controllerClass : classes) {
            Object newInstance = getNewInstance(controllerClass);
            controllers.put(controllerClass, newInstance);
        }

        return controllers;
    }

    private Object getNewInstance(Class<?> controller) {
        try {
            return controller.getDeclaredConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
