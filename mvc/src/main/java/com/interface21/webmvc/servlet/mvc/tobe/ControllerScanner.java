package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Map<Class<?>, Object> controllers = new HashMap<>();

    public ControllerScanner(final Object... basePackage)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        initialize(basePackage);
    }

    private void initialize(final Object... basePackage)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : controllers) {
            Object instance = ReflectionUtils.accessibleConstructor(controller).newInstance();
            this.controllers.put(controller, instance);
        }
    }

    public Map<Class<?>, Object> getControllers() {
        return Map.copyOf(controllers);
    }
}
