package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Set<Class<?>> getControllers() {
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    public Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> controllerTypes) {
        final Map<Class<?>, Object> instances = new HashMap<>();
        for (Class<?> type : controllerTypes) {
            try {
                instances.put(type, type.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                throw new IllegalStateException("Failed to instantiate controller: " + type, e);
            }
        }
        return instances;
    }

    public Set<Method> getRequestMappingMethods(final Class<?> controllerType) {
        return ReflectionUtils.getAllMethods(controllerType, ReflectionUtils.withAnnotation(RequestMapping.class));
    }
}
