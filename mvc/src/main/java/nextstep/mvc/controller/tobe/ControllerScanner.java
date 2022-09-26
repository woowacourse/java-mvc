package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (final Class<?> aClass : classes) {
            controllers.put(aClass, newInstance(aClass));
        }

        return controllers;
    }

    private Object newInstance(final Class<?> aClass) {
        try {
            return aClass.getConstructor().newInstance();
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException |
                       NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
