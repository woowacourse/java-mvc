package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);
    private final Reflections reflections;

    public ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateController(controllerClasses);
    }

    private Map<Class<?>, Object> instantiateController(Set<Class<?>> controllerClasses) {
        Map<Class<?>, Object> controllers = new HashMap<>();

        for (Class<?> controller : controllerClasses) {
            Object instanceController = instantiateController(controller);
            if (instanceController == null) continue;

            controllers.put(controller, instanceController);
        }

        return controllers;
    }

    private static Object instantiateController(Class<?> controller) {
        try {
            Constructor<?> constructor = controller.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException exception) {
            log.error("생성자를 호출할 수 없습니다.");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException exception) {
            log.error("인스턴스를 생성할 수 없습니다.");
        }

        return null;
    }
}
