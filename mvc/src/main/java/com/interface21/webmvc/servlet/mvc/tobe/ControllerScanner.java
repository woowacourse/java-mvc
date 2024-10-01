package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Map<Class<?>, Object> controllers;

    private final Object[] basePackage;

    public ControllerScanner(Object[] basePackage) {
        this.basePackage = basePackage;
        controllers = initialize();
    }

    private Map<Class<?>, Object> initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> annotatedClass = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(annotatedClass);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controller) {
        Map<Class<?>, Object> controllerMapping = new HashMap<>();
        try {
            for (Class<?> clazz : controller) {
                Constructor<?> constructor = clazz.getDeclaredConstructor();
                controllerMapping.put(clazz, constructor.newInstance());
            }
        } catch (NoSuchMethodException e) {
            throw new ControllerScannerException("Controller의 생성자를 찾을 수 없습니다.: " + e.getCause().getMessage());
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new ControllerScannerException("Controller 인스턴스화를 실패했습니다.: " + e.getCause().getMessage());
        }
        return controllerMapping;
    }

    public Map<Class<?>, Object> getControllers() {
        return controllers;
    }
}
