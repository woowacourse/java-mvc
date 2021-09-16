package nextstep.mvc.controller;

import nextstep.mvc.utils.AnnotationScanner;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllers) throws ReflectiveOperationException {
        Map<Class<?>, Object> result = new HashMap<>();
        for (Class clazz : controllers) {
            Object object = clazz.getConstructor().newInstance();
            result.put(clazz, object);
        }
        return result;
    }

    public Map<Class<?>, Object> getControllers(Object[] packagePath) throws ReflectiveOperationException {
        Reflections reflections = new Reflections(packagePath);
        Set<Class<?>> controllers =  AnnotationScanner.scanClassWith(packagePath, Controller.class);
        return instantiateControllers(controllers);
    }
}
