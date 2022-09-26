package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.controller.tobe.exception.InvalidReflectionException;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        final Map<Class<?>, Object> controllers = new HashMap<>();

        for (Class<?> controllerClass : controllerClasses) {
            controllers.put(controllerClass, createController(controllerClass));
        }

        return controllers;
    }

    private Object createController(Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch(NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new InvalidReflectionException();
        }
    }
}
