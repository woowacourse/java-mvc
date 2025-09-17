package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() throws Exception {
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllerClasses);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> controllerClasses) throws Exception {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> controllerClass : controllerClasses) {
            Constructor<?> controllerConstructor = controllerClass.getDeclaredConstructor();
            controllers.put(controllerClass, controllerConstructor.newInstance());
        }
        return controllers;
    }
}
