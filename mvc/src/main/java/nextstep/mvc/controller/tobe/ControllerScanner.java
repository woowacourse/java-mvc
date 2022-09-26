package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;
    private final Map<Class<?>, Object> controllers;

    public ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
        this.controllers = new HashMap<>();
        initialize();
    }

    private void initialize() {
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : controllerClasses) {
            try {
                this.controllers.put(controller, controller.getConstructor().newInstance());
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public Map<Class<?>, Object> getControllers() {
        return controllers;
    }
}
