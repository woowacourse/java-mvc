package nextstep.mvc.controller.tobe;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.controller.tobe.exception.NotSupportInstantiateControllerException;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RestController;
import org.reflections.Reflections;

public class ControllerScanner {
    private final Reflections reflections;

    public ControllerScanner(final Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> controllers = getAllControllers(reflections);
        return instantiateControllers(controllers);
    }

    private Set<Class<?>> getAllControllers(final Reflections reflections) {
        final Set<Class<?>> controllerClazz = reflections.getTypesAnnotatedWith(Controller.class);
        controllerClazz.addAll(reflections.getTypesAnnotatedWith(RestController.class));

        return controllerClazz;
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> controllers) {
        final Map<Class<?>, Object> controllerInstantiates = new HashMap<>();
        for (Class<?> controllerClass : controllers) {
            controllerInstantiates.put(controllerClass, getInstance(controllerClass));
        }
        return controllerInstantiates;
    }

    private Object getInstance(final Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new NotSupportInstantiateControllerException();
        }
    }
}
