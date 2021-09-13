package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
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

    public ControllerScanner(Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        return instantiateControllers(controllers);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllers) {
        Map<Class<?>, Object> result = new HashMap<>();

        for (Class<?> controller : controllers) {
            try {
                result.put(controller, controller.getConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new IllegalArgumentException();
            }
        }

        return result;
    }

}
