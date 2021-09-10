package nextstep.mvc.controller.tobe;

import java.util.HashSet;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    public static Set<Object> scan(Object... basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerTypes = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Object> controllers = new HashSet<>();
        for (Class<?> controllerType : controllerTypes) {
            try {
                controllers.add(controllerType.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                log.info(e.getMessage());
            }
        }
        return controllers;
    }
}
