package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Object[] basePacages) {
        this.reflections = new Reflections(basePacages);
    }

    public Map<Class<?>, Object> getControllers() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllers);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> classes) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Map<Class<?>, Object> classByInstance = new HashMap<>();
        for (Class<?> clazz : classes) {
            Object instance = clazz.getConstructor().newInstance();
            classByInstance.put(clazz, instance);
        }
        return classByInstance;
    }
}
