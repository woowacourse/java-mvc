package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private Reflection reflection;

    public ControllerScanner(final Object... targetPackage) {
        reflection = new ReflectionsLibrary(targetPackage);
    }

    public Map<Class<?>, Object> scanController() {
        Set<Class<?>> controllerClasses = reflection.getTypesAnnotatedWith(nextstep.web.annotation.Controller.class);
        return instantiateControllers(controllerClasses);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> classes) {
        Map<Class<?>, Object> controllers = new HashMap<>();
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
