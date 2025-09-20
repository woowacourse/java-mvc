package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    Reflections reflections;
    Map<Class<?>, Object> controllerInstance = new HashMap<>();

    public ControllerScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
        instantiateControllers();
    }

    public Map<Class<?>, Object> getControllers() {
        return controllerInstance;
    }

    private void instantiateControllers() {
        scanControllers().forEach(this::registerController);
    }

    private Set<Class<?>> scanControllers() {
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void registerController(Class<?> controllerClass) {
        try {
            Object controllerNewInstance = controllerClass.getDeclaredConstructor().newInstance();
            controllerInstance.put(controllerNewInstance.getClass(), controllerNewInstance);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to initialize controller: " + controllerClass.getName(), e);
        }
    }
}
