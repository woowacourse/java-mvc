package nextstep.mvc.controller.tobe;

import nextstep.mvc.exception.ControllerScanException;
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

    public static Map<Class<?>, Object> getControllers(Object[] basePackage) {
        log.info("Scan Controllers In {} !", basePackage);
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllerClasses);
    }

    private static Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllerClasses) {
        try {
            Map<Class<?>, Object> controllers = new HashMap<>();
            for (Class<?> controllerClass : controllerClasses) {
                Object controller = controllerClass.getConstructor().newInstance();
                controllers.put(controllerClass, controller);
            }
            return controllers;
        } catch (Exception e) {
            throw new ControllerScanException("어노테이션 기반 Controller를 찾는데 실패하였습니다.");
        }
    }
}
