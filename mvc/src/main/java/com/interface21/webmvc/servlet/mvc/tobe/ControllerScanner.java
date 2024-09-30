package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.context.stereotype.Controller;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final Reflections reflections;

    public ControllerScanner(final Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        try {
            return instantiateControllers(controllers);
        } catch (final NoSuchMethodException e) {
            log.error("컨트롤러 생성자 가져오기 실패 : {}", e);
            throw new IllegalArgumentException("컨트롤러 생성자가 존재하지 않습니다.");
        } catch (final InvocationTargetException | InstantiationException | IllegalAccessException e) {
            log.error("컨트롤러 인스턴스화 실패 : {}", e);
            throw new IllegalArgumentException("컨트롤러 인스턴스화에 실패했습니다.");
        }
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> controllers)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (controllers == null) {
            throw new IllegalArgumentException("컨트롤러가 존재하지 않습니다.");
        }
        final Map<Class<?>, Object> instances = new HashMap<>();
        for (Class<?> controller : controllers) {
            final Object instance = controller.getDeclaredConstructor().newInstance();
            instances.put(controller, instance);
        }
        return instances;
    }
}
