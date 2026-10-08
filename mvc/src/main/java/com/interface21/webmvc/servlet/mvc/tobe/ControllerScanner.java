package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    private ControllerScanner(final Reflections reflections) {
        this.reflections = reflections;
    }

    public static ControllerScanner from(final Object[] basePackage) {
        return new ControllerScanner(new Reflections(basePackage));
    }

    public Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        return instantiateControllers(controllers);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> controllers) {
        final Map<Class<?>, Object> controllerInstanceMap = new HashMap<>();

        controllers.forEach(controller ->
            controllerInstanceMap.put(controller, getControllerInstance(controller)));

        return controllerInstanceMap;
    }

    private Object getControllerInstance(final Class<?> controller) {
        try {
            return controller.getConstructor()
                .newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
