package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

class ControllerScanner {

    private final Reflections reflections;
    private final Map<Class<?>, Object> controllers = new HashMap<>();

    public ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public void initialize() {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : controllers) {
            try {
                this.controllers.put(controller, controller.getConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Map<Class<?>, Object> getControllers() {
        return controllers;
    }
}
