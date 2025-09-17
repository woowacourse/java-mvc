package com.interface21.webmvc.servlet;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    /**
     * basePackages를 스캔 후
     * 주어진 클래스 Set의 인스턴스를 생성하여 맵 형태로 반환
     */
    public static Map<Class<?>, Object> getControllers(Object[] basePackages)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final Reflections reflections = new Reflections(basePackages);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllerClasses);
    }

    private static Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllerClasses)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (final Class<?> controllerClass : controllerClasses) {
            controllers.put(controllerClass, controllerClass.getDeclaredConstructor().newInstance());
        }
        return controllers;
    }
}
