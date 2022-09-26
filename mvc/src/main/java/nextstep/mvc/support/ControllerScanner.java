package nextstep.mvc.support;

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

    public ControllerScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllers);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllers) {
        Map<Class<?>, Object> controllerInstances = new HashMap<>();
        try {
            for (Class<?> controller : controllers) {
                Object instance = controller.getConstructor().newInstance();
                controllerInstances.put(controller, instance);
            }
        } catch (Exception e) {
            log.warn("Failed to instantiate controllers");
            throw new RuntimeException(e);
        }
        return controllerInstances;
    }
}
