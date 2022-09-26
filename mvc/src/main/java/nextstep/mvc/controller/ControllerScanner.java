package nextstep.mvc.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Map<Class<?>, Object> controllers = new HashMap<>();

    public ControllerScanner(final Object[] basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        initialize(controllers);
    }

    private void initialize(final Set<Class<?>> controllers) {
        controllers.forEach(it -> this.controllers.put(it, newInstance(it)));
    }

    private Object newInstance(final Class<?> controller) {
        try {
            return controller.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<Class<?>, Object> getControllers() {
        return controllers;
    }
}
