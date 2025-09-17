package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Object... basePackage) {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder()
                .forPackages(Arrays.stream(basePackage)
                        .map(Object::toString)
                        .toArray(String[]::new));
        this.reflections = new Reflections(configurationBuilder);
    }

    public Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllerClasses);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllerClasses) {
        HashMap<Class<?>, Object> controllerInstances = new HashMap<>();
        try {
            for (Class<?> controllerClass : controllerClasses) {
                final Object controllerInstance = controllerClass.getConstructor().newInstance();
                controllerInstances.put(controllerClass, controllerInstance);
            }
        } catch (NoSuchMethodException |
                 InstantiationException |
                 IllegalAccessException |
                 InvocationTargetException ex) {
            throw new IllegalArgumentException("Controller 객체를 생성하는 도중 예외가 발생했습니다.", ex);
        }
        return controllerInstances;
    }
}
