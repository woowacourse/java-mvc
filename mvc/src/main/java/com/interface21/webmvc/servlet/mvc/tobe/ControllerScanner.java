package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Object[] basePackage;

    public ControllerScanner(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public Map<Class<?>, Object> scan() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        final Map<Class<?>, Object> controllerByClasses = new HashMap<>();
        try {
            for (Class<?> controllerClass : controllerClasses) {

                Object controller = controllerClass.getDeclaredConstructor().newInstance();
                controllerByClasses.putIfAbsent(controllerClass, controller);
            }
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("can not search method");
        } catch (Exception e) {
            throw new IllegalStateException("controllerscanner failure, e : " + e);
        }
        return controllerByClasses;
    }
}
