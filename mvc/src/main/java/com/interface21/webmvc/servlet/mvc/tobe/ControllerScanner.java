package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final Map<Class<?>, Object> controllers = new ConcurrentHashMap<>();

    public ControllerScanner(Object[] basePackage) {
        instantiateControllers(basePackage);
    }

    private Map<Class<?>, Object> instantiateControllers(Object[] basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        controllerClasses.forEach(controllerClass -> controllers.put(controllerClass, createInstance(controllerClass)));
        return controllers;
    }

    private Object createInstance(Class<?> controllerClass) {
        try {
            return ReflectionUtils.accessibleConstructor(controllerClass).newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            log.error("Failed to create instance by controller: {}", controllerClass.getName(), e);
            throw new RuntimeException(e);
        }
    }

    public Map<Class<?>, Object> getControllers() {
        return controllers;
    }
}
