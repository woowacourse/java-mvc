package nextstep.mvc.scanner;

import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(typesAnnotatedWith);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> typesAnnotatedWith) {
        Map<Class<?>, Object> controllers = new ConcurrentHashMap<>();
        for (Class<?> annotatedClass : typesAnnotatedWith) {
            try {
                Object controller = annotatedClass.getConstructor().newInstance();
                controllers.put(annotatedClass, controller);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return controllers;
    }
}
