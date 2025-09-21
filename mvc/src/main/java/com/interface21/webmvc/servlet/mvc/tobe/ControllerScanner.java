package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final Reflections reflections;

    public ControllerScanner(Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        log.info("스캔된 @Controller 클래스 수: {}", controllerClasses.size());
        controllerClasses.forEach(clazz -> log.debug("발견된 컨트롤러: {}", clazz.getName()));

        try {
            return instantiateControllers(controllerClasses);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            log.error("컨트롤러 인스턴스화에 실패했습니다 : {}", e.getMessage(), e);
        }

        return null;
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllerClasses)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final Map<Class<?>, Object> controllers = new HashMap<>();

        for (Class<?> controllerClass : controllerClasses) {
            Constructor<?> controller = controllerClass.getDeclaredConstructor();
            controller.setAccessible(true);
            Object instance = controller.newInstance();
            log.info("컨트롤러 인스턴스 생성 완료: {}", controllerClass.getName());
            controllers.put(controllerClass, instance);
        }

        log.info("총 {}개의 컨트롤러 인스턴스화 완료", controllers.size());
        return controllers;
    }
}
