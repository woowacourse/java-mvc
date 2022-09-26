package nextstep.mvc.controller.tobe;

import java.lang.reflect.Constructor;
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

    public ControllerScanner(Object[] basePackage) {
        this(new Reflections(basePackage));
    }

    private ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllers);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> controllers) {
        final Map<Class<?>, Object> controllerInstance = new HashMap<>();
        controllers.forEach(controller -> controllerInstance.put(controller, getControllerInstance(controller)));
        return controllerInstance;
    }

    private Object getControllerInstance(Class<?> controller) {
        try {
            final Constructor<?> constructor = controller.getConstructor();
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException
                | InvocationTargetException | NoSuchMethodException e) {
            log.warn(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }
}
