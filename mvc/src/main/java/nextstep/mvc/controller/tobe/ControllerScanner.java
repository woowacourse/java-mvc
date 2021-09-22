package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerScanner.class);

    private final Reflections reflections;

    public ControllerScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiate(controllerClasses);
    }

    private Map<Class<?>, Object> instantiate(Set<Class<?>> controllerClasses) {
        Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> controllerClass : controllerClasses) {
            try {
                Object controller = controllerClass.getDeclaredConstructor().newInstance();
                controllers.put(controllerClass, controller);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                LOGGER.error("there's no controller");
                throw new IllegalStateException("there's no controller");
            }
        }
        return controllers;
    }
}
