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
    private final Reflections reflections = new Reflections("com.techcourse.controller");

    private ControllerScanner() {
    }

    public Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllers);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllers) {
        final Map<Class<?>, Object> controllerMap = new HashMap<>();

        controllers.forEach(controller -> {
            try {
                controllerMap.put(controller, controller.getConstructor().newInstance());
            } catch (Exception e) {
                log.error("Exception : {}", e.getMessage(), e);
                throw new IllegalArgumentException();
            }
        });
        return controllerMap;
    }
}
