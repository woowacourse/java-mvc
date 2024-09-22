package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class Controllers {

    private final Map<Class<?>, Object> controllers = new HashMap<>();

    public Controllers(Object[] basePackage) {
        try {
            Reflections reflections = new Reflections(basePackage);
            Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
            for (Class<?> controllerClass : controllerClasses) {
                controllers.put(controllerClass, controllerClass.getDeclaredConstructor().newInstance());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    public Object getController(Class<?> controllerClass) {
        return controllers.get(controllerClass);
    }

    public Set<Class<?>> getControllerClasses() {
        return controllers.keySet();
    }
}
