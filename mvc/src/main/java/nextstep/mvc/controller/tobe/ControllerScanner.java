package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    private ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public static ControllerScanner from(Object... basePackage) {
        return new ControllerScanner(new Reflections(basePackage));
    }

    public Map<Class<?>, Object> getControllers()
            throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        return initiateControllers(controllers);
    }

    private Map<Class<?>, Object> initiateControllers(Set<Class<?>> controllers)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<Class<?>, Object> initiatedControllers = new HashMap<>();
        for (Class<?> controller : controllers) {
            Object instance = controller.getDeclaredConstructor()
                    .newInstance();
            initiatedControllers.put(controller, instance);
        }
        return initiatedControllers;
    }
}
