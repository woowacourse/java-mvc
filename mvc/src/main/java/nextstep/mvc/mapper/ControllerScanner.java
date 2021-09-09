package nextstep.mvc.mapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private ControllerScanner() {
    }

    public static Map<Class<?>, Object> getControllers(Object[] basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return createControllerInstance(controllerClasses);
    }

    private static Map<Class<?>, Object> createControllerInstance(Set<Class<?>> controllerClasses) {
        try {
            Map<Class<?>, Object> controllers = new HashMap<>();
            for (Class<?> controllerClass : controllerClasses) {
                Object controller = controllerClass.getConstructor().newInstance();
                controllers.put(controllerClass, controller);
            }
            return controllers;
        } catch (Exception e) {
            throw new RuntimeException("Annotation으로 선언된 Controller가 없습니다.");
        }
    }
}
