package nextstep.mvc.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import nextstep.web.annotation.Controller;

public class ControllerScanner {

    private final Object[] basePackages;

    public ControllerScanner(final Object[] basePackages) {
        this.basePackages = basePackages;
    }

    public Map<Class<?>, Object> getHandlerExecutions() {
        final Set<Class<?>> classes = collectControllerClasses();
        return instantiateControllers(classes);
    }

    private Set<Class<?>> collectControllerClasses() {
        final Reflections reflections = new Reflections(basePackages);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> classes) {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (final var clazz : classes) {
            final Object controller = instantiateController(clazz);
            controllers.put(clazz, controller);
        }
        return controllers;
    }

    private Object instantiateController(final Class<?> clazz) {
        try {
            final var emptyConstructor = clazz.getConstructor();
            return emptyConstructor.newInstance();

        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodError("빈 생성자를 조회할 수 없습니다.");

        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("객체를 생성할 수 없습니다.");
        }
    }
}
