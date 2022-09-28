package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.controller.exception.CreateObjectException;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

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
            log.debug("Object To Create : {}", controller.getName());

            Object handler = controller.getDeclaredConstructor().newInstance();
            instancedControllers.put(controller, handler);
        }
        return instancedControllers;
    }
}
