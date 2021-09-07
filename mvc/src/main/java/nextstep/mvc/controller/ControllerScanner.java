package nextstep.mvc.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerScanner.class);

    private final Reflections reflections;

    public ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() throws Exception {
        Set<Class<?>> annotatedControllers = reflections.getTypesAnnotatedWith(Controller.class);

        return instantiateControllers(annotatedControllers);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> annotatedControllers) throws Exception {
        Map<Class<?>, Object> controllers = new HashMap<>();

        for (Class<?> clazz : annotatedControllers) {
            controllers.put(clazz, clazz.getConstructor().newInstance());
            LOG.debug("Scanning Controller: {}", clazz.getSimpleName());
        }

        return controllers;
    }
}
