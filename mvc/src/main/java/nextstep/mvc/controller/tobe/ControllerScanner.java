package nextstep.mvc.controller.tobe;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllerClasses);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllerClasses) {
        Map<Class<?>, Object> controllers = new HashMap<>();

        for (Class<?> controllerClass : controllerClasses) {
            Object controller = instantiateController(controllerClass);
            controllers.put(controllerClass, controller);
        }

        return controllers;
    }

    private Object instantiateController(Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot instantiate controller");
        }
    }
}
