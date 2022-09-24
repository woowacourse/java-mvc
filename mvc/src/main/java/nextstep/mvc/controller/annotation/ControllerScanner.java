package nextstep.mvc.controller.annotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private static final Class<Controller> CONTROLLER_ANNOTATION_CLASS = Controller.class;

    private final Reflections reflections;

    public ControllerScanner(final Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(CONTROLLER_ANNOTATION_CLASS);
        return instantiateControllers(classes);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> classes) {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> controller : classes) {
            controllers.put(controller, getInstance(controller));
        }
        return controllers;
    }

    private Object getInstance(final Class<?> clazz) {
        try {
            final Constructor<?> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException exception) {
            throw new IllegalArgumentException("생성자를 가져올 수 없습니다. " + clazz.getName());
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException exception){
            throw new IllegalArgumentException("인스턴스화할 수 없습니다. " + clazz.getName());
        }
    }
}
