package nextstep.mvc.controller.tobe;

import nextstep.web.annotation.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    public static Map<Class<?>, Object> getControllers(final Object[] basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllers);
    }

    private static Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllers) {
        final HashMap<Class<?>, Object> controllerInstances = new HashMap<>();
        for (final Class<?> controller : controllers) {
            controllerInstances.put(controller, instantiateController(controller));
        }
        return controllerInstances;
    }

    private static Object instantiateController(Class<?> controller) {
        try {
            return controller.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            log.warn("Cannot generate controller instance!");
            throw new IllegalStateException();
        }
    }
}