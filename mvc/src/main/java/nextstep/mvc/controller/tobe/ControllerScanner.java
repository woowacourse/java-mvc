package nextstep.mvc.controller.tobe;

import nextstep.web.annotation.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final String basePackage;
    private final Set<Object> controllers;

    public ControllerScanner(String basePackage) {
        this.basePackage = basePackage;
        this.controllers = new HashSet<>();
    }

    public Set<Object> getControllers() {
        final Set<Class<?>> controllerTypes = getControllerTypes();
        for (Class<?> controllerType : controllerTypes) {
            try {
                controllers.add(controllerType.getDeclaredConstructor().newInstance());
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                log.info("AnnotationHandlerMapping#initialize, 인스턴스화를 할 수 없습니다.");
                throw new IllegalStateException("Controller의 인스턴스화 과정에서 문제가 발생했습니다.");
            }
        }
        return controllers;
    }

    public Set<Class<?>> getControllerTypes() {
        log.info("Scan @Controller from basePackage#{}", basePackage);
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }
}
