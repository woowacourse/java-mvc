package nextstep.mvc.controller.tobe;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final Reflections reflections;

    public ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        try {
            return instantiateControllers(classes);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
        }
        return Map.of();
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> classes) throws Exception {
        Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> clazz : classes) {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            controllers.put(clazz, instance);
        }
        return controllers;
    }
}
