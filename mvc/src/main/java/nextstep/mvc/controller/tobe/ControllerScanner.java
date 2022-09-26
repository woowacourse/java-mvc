package nextstep.mvc.controller.tobe;

import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Reflections reflections;
    private final Set<Class<?>> controllers;

    public ControllerScanner(final Object... basePackage) {
        log.info("Initialized ControllerScanner!");

        reflections = new Reflections(basePackage);
        controllers = reflections.getTypesAnnotatedWith(Controller.class);
    }

    public Set<Class<?>> getControllers() {
        return controllers;
    }
}
