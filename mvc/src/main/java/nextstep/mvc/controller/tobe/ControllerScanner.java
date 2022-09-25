package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.controller.exception.CreateObjectException;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    public Map<Class<?>, Object> getControllers(final Object[] basePackage) {
        try {
            Reflections reflections = new Reflections(basePackage);
            Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
            return instantiateControllers(controllers);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new CreateObjectException();
        }
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> controllers)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Map<Class<?>, Object> instancedControllers = new HashMap<>();

        for (Class<?> controller : controllers) {
            Object handler = controller.getDeclaredConstructor().newInstance();
            instancedControllers.put(controller, handler);
        }
        return instancedControllers;
    }
}
