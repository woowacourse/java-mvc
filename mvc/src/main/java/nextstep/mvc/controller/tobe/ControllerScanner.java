package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    final Map<Class<?>, Object> controllers = new HashMap<>();

    public ControllerScanner(Object... basePackage) {
        setControllers(basePackage);
    }

    private void setControllers(Object[] basePackage) {
        if (basePackage.length != 0) {
            for (Object targetPackage : basePackage) {
                Reflections reflections = new Reflections(targetPackage);
                Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
                instantiateControllers(controllers);
            }
            return;
        }

        Reflections reflections = new Reflections();
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        instantiateControllers(controllers);
    }

    private void instantiateControllers(Set<Class<?>> controllers) {
        for (Class<?> controller : controllers) {
            try {
                this.controllers.put(controller, controller.getConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.error("Exception : {}", e.getMessage(), e);
            }
        }
    }

    public Map<Class<?>, Object> getControllers() {
        return Collections.unmodifiableMap(controllers);
    }
}
