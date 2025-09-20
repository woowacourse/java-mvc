package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Map<Class<?>, Object> controllers = findControllers();

    public Map<Class<?>, Object> findControllers() {
        Reflections reflections = new Reflections("com.techcourse.controller");
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(Controller.class);

        final Map<Class<?>, Object> instances = new HashMap<>();
        for (Class<?> type : types) {
            try {
                Constructor<?> constructor = type.getDeclaredConstructor();
                Object instance = constructor.newInstance();
                instances.put(type, instance);
            } catch (NoSuchMethodException e) {
                System.out.println("No Such Method");
            } catch (Exception e){
                throw new IllegalStateException();
            }
        }

        return instances;
    }

}

