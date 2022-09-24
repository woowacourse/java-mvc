package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Map<Class<?>, Object> controllers;

    public ControllerScanner(final Object... basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        controllers = instantiateControllers(controllerClasses);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> classes) {
        Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> clazz : classes) {
            controllers.put(clazz, createInstance(clazz));
        }
        return controllers;
    }

    private Object createInstance(final Class<?> clazz) {
        try {
            return clazz.getConstructor()
                    .newInstance();
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<Class<?>, Object> getControllers() {
        return controllers;
    }
}
