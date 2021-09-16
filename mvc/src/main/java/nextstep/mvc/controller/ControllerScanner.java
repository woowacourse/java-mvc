package nextstep.mvc.controller;

import nextstep.web.annotation.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ControllerScanner {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerScanner.class);

    private final Object[] basePackage;
    private final Map<Class<?>, Object> controllers;

    public ControllerScanner(Object... basePackage) {
        this.basePackage = basePackage;
        controllers = new HashMap<>();
    }

    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerClassesWithControllerAnnotation = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controllerClass : controllerClassesWithControllerAnnotation) {
            final Object controllerInstance = getControllerInstance(controllerClass);
            controllers.put(controllerClass, controllerInstance);
        }
    }

    private Object getControllerInstance(Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (final InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException e) {
            LOG.error("Contoller Annotation이 달려있는 클래스의 인스턴스 생성에 실패했습니다. 클래스 : {}", controllerClass.getName());
            throw new IllegalStateException("Contoller Annotation이 달려있는 클래스의 인스턴스 생성에 실패했습니다. 클래스 : " + controllerClass.getName());
        }
    }

    public List<Object> getControllers() {
        return new ArrayList<>(controllers.values());
    }
}
