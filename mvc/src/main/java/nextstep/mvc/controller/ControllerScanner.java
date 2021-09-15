package nextstep.mvc.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(String packages) {
        this.reflections = new Reflections(packages);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        return instantiateControllers(controllers);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllers) {
        Map<Class<?>, Object> controllerMap = new HashMap<>();
        try {
            for (Class<?> controller : controllers) {
                controllerMap.put(controller, controller.getConstructor().newInstance());
            }
            return controllerMap;
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }

    }
}
