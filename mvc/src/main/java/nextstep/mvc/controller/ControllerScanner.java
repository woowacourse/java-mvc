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

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final Reflections reflections;

    public ControllerScanner(final Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllers);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllers) {
        try {
            Map<Class<?>, Object> controllerInstances = new HashMap<>();
            for (Class<?> clazz : controllers) {
                controllerInstances.put(clazz, clazz.getDeclaredConstructor().newInstance());
            }
            return controllerInstances;
        } catch (NoSuchMethodException |
                 InvocationTargetException |
                 InstantiationException |
                 IllegalAccessException exception) {
            log.error(exception.getMessage());
            throw new RuntimeException();
        }
    }
}
