package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    final Map<Class<?>, Object> controllers = new HashMap<>();

    public ControllerScanner(Object... basePackage) {
        scanControllers(basePackage);
    }

    private void scanControllers(Object... basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : controllerClasses) {
            Object instance = createInstance(clazz);
            controllers.put(clazz, instance);
        }
    }

    private Object createInstance(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Can't create instance of class : " + clazz.getName(), e);
        }
    }
}
