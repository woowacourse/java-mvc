package nextstep.mvc.controller.tobe;

import nextstep.web.annotation.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private ControllerScanner() {
    }

    public static Map<Class<?>, Object> scanControllersInPackage(Object[] basePackage) {
        log.info("Scan Controllers In {} !", basePackage);
        Map<Class<?>, Object> controllers = new HashMap<>();
        try {
            Reflections reflections = new Reflections(basePackage);
            Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
            for (Class<?> controllerClass : controllerClasses) {
                Object controller = controllerClass.getConstructor().newInstance();
                controllers.put(controllerClass, controller);
            }
        } catch (Exception e) {
            log.error("Scan Controllers Fail!", e);
        }
        return controllers;
    }
}
