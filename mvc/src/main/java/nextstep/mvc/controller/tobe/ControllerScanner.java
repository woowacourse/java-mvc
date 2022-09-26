package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import nextstep.web.annotation.Controller;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers(){
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        return instantiate(controllers);
    }

    private Map<Class<?>, Object> instantiate(Set<Class<?>> controllers) {
        final Map<Class<?>, Object> instances = new HashMap<>();
        for (Class<?> clazz : controllers) {
            putInstances(instances, clazz);
        }

        return instances;
    }

    private void putInstances(Map<Class<?>, Object> instances, Class<?> clazz) {
        try {
            final Object instance = clazz.getConstructor().newInstance();
            instances.put(clazz, instance);
        } catch (Exception e) {
            throw new NoSuchMethodError("생성자가 존재하지 않습니다.");
        }
    }
}
