package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.stream.Collectors;
import org.reflections.Reflections;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> instantiateControllers() {
        return reflections.getTypesAnnotatedWith(Controller.class).stream()
                .collect(Collectors.toMap(clazz -> clazz, this::instantiate));
    }

    private Object instantiate(Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            log.error("Failed to instantiate controller: " + clazz, e);
            throw new IllegalStateException();
        }
    }
}
