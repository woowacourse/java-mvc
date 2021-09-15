package nextstep.mvc;

import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    public static Map<Class<?>, Object> scan(Object[] basePackage) throws Exception {
        Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        return initializeControllerMap(controllers);
    }

    private static Map<Class<?>, Object> initializeControllerMap(Set<Class<?>> controllers) throws Exception {
        final Map<Class<?>, Object> controllerMap = new HashMap<>();
        for (Class<?> controller : controllers) {
            final Object controllerInstance = controller.getDeclaredConstructor().newInstance();
            controllerMap.put(controller, controllerInstance);
        }
        return controllerMap;
    }
}
