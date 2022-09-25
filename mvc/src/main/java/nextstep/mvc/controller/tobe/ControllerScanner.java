package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.sql.Ref;
import java.util.HashMap;
import java.util.HashSet;
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
        final Set<Class<?>> handlerClasses = new HashSet<>(reflections.getTypesAnnotatedWith(Controller.class));
        return instantiateControllers(handlerClasses);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> controllerClasses) {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> controllerClass : controllerClasses) {
            final Object controller = getControllerInstance(controllerClass);
            controllers.put(controllerClass, controller);
        }
        return controllers;
    }

    private Object getControllerInstance(final Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (NoSuchMethodException
                | InvocationTargetException
                | InstantiationException
                | IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
