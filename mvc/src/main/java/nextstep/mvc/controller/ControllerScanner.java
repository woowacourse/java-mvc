package nextstep.mvc.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Reflections reflections;

    public ControllerScanner(Object object) {
        this.reflections = new Reflections((String) object);
    }

    public Map<Class<?>, Object> getController() {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        final Set<Class<?>> annotatedControllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> annotatedController : annotatedControllers) {
            try {
                controllers
                        .put(annotatedController,
                                annotatedController.getConstructor().newInstance());
            } catch (Exception e) {
                log.debug("Error: {}", e.getMessage(), e);
            }
        }
        return controllers;
    }
}
