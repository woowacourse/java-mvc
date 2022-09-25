package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private Object[] targetPackage;

    public ControllerScanner(final Object... targetPackage) {
        this.targetPackage = targetPackage;
    }

    public Map<Class<?>, Object> scanController() {
        Reflections reflections = new Reflections(targetPackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(nextstep.web.annotation.Controller.class);
        return instantiateControllers(controllerClasses);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> classes) {
        Map< Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> clazz : classes) {
            controllers.put(clazz, createInstance(clazz));
        }
        return controllers;
    }

    private Object createInstance(final Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
