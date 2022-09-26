package nextstep.mvc.controller.tobe;

import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> targetClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiate(targetClasses);
    }

    private Map<Class<?>, Object> instantiate(Set<Class<?>> classes) {
        Map<Class<?>, Object> controllers = new HashMap<>();
        classes.forEach(each -> {
            try {
                controllers.put(each, each.getConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
        return controllers;
    }
}
