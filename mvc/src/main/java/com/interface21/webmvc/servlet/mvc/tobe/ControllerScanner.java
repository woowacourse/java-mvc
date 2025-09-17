package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {
    private final Reflections reflections;

    public ControllerScanner(Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllers);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllers) {
        Map<Class<?>, Object> controllerRegistry = new HashMap<>();

        for (Class<?> controller : controllers) {
            Object controllerInstance = instantiateController(controller);
            controllerRegistry.put(controller, controllerInstance);
        }
        return controllerRegistry;
    }

    private void makeAccessible(Constructor<?> constructor) {
        if (!constructor.canAccess(null)) {
            constructor.setAccessible(true);
        }
    }

    private Object instantiateController(Class<?> controller) {
        try {
            Constructor<?> constructor = controller.getDeclaredConstructor();
            makeAccessible(constructor);
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("컨트롤러에 기본 생성자가 없습니다: " + controller.getName());
        } catch (InstantiationException e) {
            throw new IllegalStateException("컨트롤러 인스턴스를 생성할 수 없습니다: " + controller.getName());
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("컨트롤러 생성자에 접근이 불가합니다: " + controller.getName());
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException());
        }
    }
}
