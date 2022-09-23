package nextstep.mvc.controller.tobe;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.exception.NoDefaultConstructorException;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Reflections reflections;

    public ControllerScanner(final Object[] basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    public Map<Class<?>, Object> getControllers() {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (final Class<?> controller : controllerClasses) {
            controllers.put(controller, instantiateController(controller));
        }
        return controllers;
    }

    private Object instantiateController(final Class<?> controller) {
        try {
            return controller.getConstructor().newInstance();
        } catch (final Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new NoDefaultConstructorException();
        }
    }
}
