package nextstep.mvc.controller;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.controller.exception.ControllerInstantiationException;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    Set<Class<?>> getControllers() {
        return this.reflections.getTypesAnnotatedWith(Controller.class);
    }

    Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllers) {
        final Map<Class<?>, Object> controllerInstances = new HashMap<>();
        for (Class<?> controller : controllers) {
            addControllerInstance(controllerInstances, controller);
        }
        return controllerInstances;
    }

    private void addControllerInstance(Map<Class<?>, Object> controllers, Class<?> controller) {
        try {
            Constructor<?> constructor = controller.getConstructor();
            controllers.put(controller, constructor.newInstance());
        } catch (ReflectiveOperationException e) {
            throw new ControllerInstantiationException();
        }
    }
}
