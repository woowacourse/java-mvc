package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;
    private final InstanceInitializer instanceInitializer;

    public ControllerScanner(String basePackage) {
        this.reflections = new Reflections(basePackage);
        this.instanceInitializer = new InstanceInitializer();
    }

    public Map<Class<?>, Object> getControllers(ScanType scanType) throws ReflectiveOperationException {
        Set<Class<?>> classes = new HashSet<>(
                switch (scanType) {
                    case ANNOTATION -> reflections.getTypesAnnotatedWith(Controller.class);
                    case SUB_TYPE_HIERARCHY -> reflections.getSubTypesOf(com.interface21.webmvc.servlet.mvc.asis.Controller.class);
                });
        return instantiateControllers(classes);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> classes) throws ReflectiveOperationException {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> clazz : classes) {
            controllers.put(clazz, instanceInitializer.createInstance(clazz));
        }
        return controllers;
    }
}
