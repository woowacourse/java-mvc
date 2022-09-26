package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final Map<Class<?>, Object> controllers;

    public ControllerScanner(final Object... basePackage) {
        this.controllers = instantiateControllers(new Reflections(basePackage).getTypesAnnotatedWith(Controller.class));
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> controllerClasses) {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> controllerClass : controllerClasses) {
            addController(controllers, controllerClass);
        }
        return controllers;
    }

    private void addController(final Map<Class<?>, Object> controllers, final Class<?> controllerClass) {
        try {
            controllers.put(controllerClass, controllerClass.getDeclaredConstructor().newInstance());
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            log.error(e.getMessage());
        }
    }

    public Map<Class<?>, Object> getControllers() {
        return controllers;
    }
}
