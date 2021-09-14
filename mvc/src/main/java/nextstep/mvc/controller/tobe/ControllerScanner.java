package nextstep.mvc.controller.tobe;

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

    public Map<Class<?>, Object> getController() {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateController(controllers);
    }

    private Map<Class<?>, Object> instantiateController(Set<Class<?>> controllers) {
        final Map<Class<?>, Object> classes = new HashMap<>();
        for (Class<?> controller : controllers) {
            try {
                Object instance = controller.getDeclaredConstructor().newInstance();
                classes.put(controller, instance);
            } catch (Exception e) {
                LOG.error("Instance Create Error!! : {}", e.getMessage());
            }
        }
        return classes;
    }
}
