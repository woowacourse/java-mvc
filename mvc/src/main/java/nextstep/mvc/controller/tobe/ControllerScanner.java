package nextstep.mvc.controller.tobe;

import nextstep.web.annotation.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final Reflections reflections;

    public ControllerScanner(Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        Map<Class<?>, Object> controllers = new HashMap<>();

        return putControllerClassWithInstance(controllerClasses, controllers);
    }

    private Map<Class<?>, Object> putControllerClassWithInstance(Set<Class<?>> controllerClasses, Map<Class<?>, Object> controllers) {
        for (Class<?> controllerClass : controllerClasses) {
            try {
                Object instance = controllerClass.getConstructor().newInstance();
                controllers.put(controllerClass, instance);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.error(e.getMessage());
            }
        }
        return controllers;
    }
}
