package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ControllerContainer {

    private static final Logger log = LoggerFactory.getLogger(ControllerContainer.class);

    private final Map<Class<?>, Object> container;
    private final Object[] basePackage;

    public ControllerContainer(final Object... basePackage) {
        this.basePackage = basePackage;
        this.container = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized ControllerContainer!");
        final Reflections reflections = new Reflections(basePackage);
        final var controllers = reflections.getTypesAnnotatedWith(Controller.class);
        controllers.stream()
                .forEach(this::init);
    }

    private void init(final Class<?> controller) {
        final Object instance = getInstance(controller);
        container.put(controller, instance);
    }

    private Object getInstance(final Class<?> controller) {
        final Constructor<?> constructor = getConstructor(controller);
        return createInstance(constructor);
    }

    private Constructor<?> getConstructor(final Class<?> controller) {
        try {
            return controller.getDeclaredConstructor();
        } catch (final NoSuchMethodException e) {
            throw new NotFoundConstructorException(String.format("%s 에 대한 생성자를 찾지 못했습니다.", controller));
        }
    }

    private Object createInstance(final Constructor<?> constructor) {
        try {
            return constructor.newInstance();
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new FailInstanceCreateException(constructor, e);
        }
    }

    public Object getController(final Class<?> controller) {
        return Optional.ofNullable(container.get(controller))
                .orElseThrow(() -> new IllegalArgumentException(String.format("%s는 %s내 없는 인스턴스입니다.", controller, Arrays.toString(basePackage))));
    }

    public Map<Class<?>, Object> getControllers() {
        return container;
    }
}
