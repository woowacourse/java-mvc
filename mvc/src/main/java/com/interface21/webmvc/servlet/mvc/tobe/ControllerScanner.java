package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {
    private final Reflections reflections;

    public ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // basePackage에서 Controller 어노테이션 붙은 모든 클래스 가져오기
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllerClasses);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllerClasses)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Map<Class<?>, Object> controllers = new HashMap<>();

        for (Class<?> controllerClass : controllerClasses) {
            Object controller = controllerClass.getDeclaredConstructor().newInstance();
            controllers.put(controllerClass, controller);
        }

        return controllers;
    }
}
