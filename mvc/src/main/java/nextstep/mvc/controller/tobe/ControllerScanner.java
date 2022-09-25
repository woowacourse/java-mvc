package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        try {
            Map<Class<?>, Object> controllers = new HashMap<>();
            instantiateControllers(controllerClasses, controllers);
            return controllers;
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException();
        }
    }

    private void instantiateControllers(final Set<Class<?>> controllerClasses, final Map<Class<?>, Object> controllers)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        for (Class<?> controllerClass : controllerClasses) {
            controllers.put(controllerClass, controllerClass.getDeclaredConstructor().newInstance());
        }
    }
}
