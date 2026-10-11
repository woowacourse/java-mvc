package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private final Map<Class<?>, Object> controllers;

    public ControllerScanner(Object... basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses =  reflections.getTypesAnnotatedWith(Controller.class);
        Map<Class<?>, Object> controllerInstances = new HashMap<>();

        controllerClasses.forEach(controllerClass -> putInstance(controllerClass, controllerInstances));
        this.controllers = controllerInstances;
    }

    public Map<Class<?>, Object> getControllers() {
        return Collections.unmodifiableMap(controllers);
    }

    private void putInstance(Class<?> controller, Map<Class<?>, Object> controllerInstances) {
        try {
            Object instance = controller.getDeclaredConstructor()
                    .newInstance();
            controllerInstances.put(controller, instance);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(controller.getName() + "의 인스턴스를 만들 수 없습니다.", e);
        }
    }
}
