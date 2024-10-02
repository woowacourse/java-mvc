package com.interface21.webmvc.servlet.mvc.tobe.utils;

import com.interface21.context.stereotype.Controller;
import com.interface21.webmvc.servlet.mvc.exception.InstanceManagerInstantiationException;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class InstanceManager {

    private final Map<Class<?>, Object> controllers = new ConcurrentHashMap<>();

    public InstanceManager(Object[] basePackage) {
        scanControllers(basePackage);
    }

    private void scanControllers(Object[] basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerTypes = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> clazz : controllerTypes) {
            create(clazz);
        }
    }

    private void create(Class<?> clazz) {
        Constructor<?> constructor = getConstructor(clazz);
        Object controllerInstance = getControllerInstance(constructor);

        controllers.put(clazz, controllerInstance);
    }

    private Constructor<?> getConstructor(Class<?> clazz) {
        Constructor<?> constructor;
        try {
            constructor = clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new InstanceManagerInstantiationException("오류: 해당 메서드를 찾을 수 없습니다");
        }
        return constructor;
    }

    private Object getControllerInstance(Constructor<?> constructor) {
        Object controllerInstance;
        try {
            controllerInstance = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new InstanceManagerInstantiationException("오류: 인스턴스를 생성할 수 없습니다.");
        }
        return controllerInstance;
    }

    public Map<Class<?>, Object> getControllers() {
        return Collections.unmodifiableMap(controllers);
    }
}
