package nextstep.mvc.controller.tobe;

import nextstep.mvc.exception.CreateHandlerInstanceException;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllerClasses);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> controllerClasses) {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> controllerClass : controllerClasses) {
            final Object controller = makeController(controllerClass);
            controllers.put(controllerClass, controller);
        }
        return controllers;
    }

    private Object makeController(final Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new CreateHandlerInstanceException();
        }
    }
}
