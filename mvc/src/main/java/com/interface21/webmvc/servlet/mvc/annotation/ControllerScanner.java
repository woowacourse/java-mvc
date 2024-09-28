package com.interface21.webmvc.servlet.mvc.annotation;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Object[] basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    public Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllerClasses);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> controllerClasses) {
        final Map<Class<?>, Object> controllerInstances = new HashMap<>();
        for (Class<?> controllerClass : controllerClasses) {
            controllerInstances.put(controllerClass, instantiateController(controllerClass));
        }
        return controllerInstances;
    }

    private Object instantiateController(final Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("생성자를 찾을 수 없습니다.");
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("인스턴스화할 수 없습니다.");
        }
    }
}
