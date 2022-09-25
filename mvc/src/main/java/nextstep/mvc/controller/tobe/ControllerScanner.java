package nextstep.mvc.controller.tobe;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Set<Class<?>> controllers;

    public ControllerScanner(Object... basePackage) {
        Reflections reflections = new Reflections(basePackage);
        this.controllers = reflections.getTypesAnnotatedWith(Controller.class);
    }

    Map<Class<?>, Object> instantiateControllers() {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> controller : this.controllers) {
            putControllerInstance(controllers, controller);
        }
        return controllers;
    }

    private void putControllerInstance(Map<Class<?>, Object> controllers, Class<?> controller) {
        try {
            Constructor<?> constructor = controller.getConstructor();
            controllers.put(controller, constructor.newInstance());
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
