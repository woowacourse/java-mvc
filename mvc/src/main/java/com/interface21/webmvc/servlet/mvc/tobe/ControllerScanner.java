package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.Map;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> scan() {
        return reflections.getTypesAnnotatedWith(Controller.class)
                .stream()
                .collect(Collectors.toMap(
                        clazz -> clazz,
                        this::instantiate
                ));
    }

    private Object instantiate(final Class<?> clazz) {
        try{
            return clazz.getConstructor().newInstance();
        } catch (final Exception e) {
            log.error("Failed to instantiate controller: {}", clazz.getName(), e);
            throw new IllegalStateException("Failed to instantiate controller: " + clazz.getName(), e);
        }
    }
}
