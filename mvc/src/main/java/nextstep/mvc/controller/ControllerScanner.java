package nextstep.mvc.controller;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.exception.NoDefaultConstructorException;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllerClasses);

    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> controllers) {
        try {
            final Map<Class<?>, Object> controllerInstanceMapping = new HashMap<>();
            for (final Class<?> controller : controllers) {
                final Constructor<?> constructor = controller.getConstructor();
                controllerInstanceMapping.put(controller, constructor.newInstance());
            }
            return controllerInstanceMapping;
        } catch (final Exception e) {
            throw new NoDefaultConstructorException();
        }
    }
}
