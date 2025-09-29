package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    final Map<Class<?>, Object> controllers = new HashMap<>();
    private final Object[] basePackage;

    public ControllerScanner(final Object... basePackage)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        this.basePackage = basePackage;
        scan();
    }

    private void scan()
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> annotatedControllers = findAnnotatedControllers(reflections);

        for (Class<?> annotatedController : annotatedControllers) {
            controllers.put(annotatedController, annotatedController.getDeclaredConstructor().newInstance());
        }
    }

    private Set<Class<?>> findAnnotatedControllers(Reflections reflections) {
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    public Set<Class<?>> getControllerClass() {
        return controllers.keySet();
    }

    public Object getControllerInstance(Class<?> clazz) {
        return controllers.get(clazz);
    }
}
