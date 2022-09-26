package nextstep.mvc.controller.asis;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.controller.exception.InstantiatingFailedException;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    public static Map<Class<?>, Object> getControllers(Object[] basePackage) {
        log.info("Instantiating Annotated Controller!");
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(classes);
    }

    private static Map<Class<?>, Object> instantiateControllers(Set<Class<?>> classes) {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> aClass : classes) {
            instantiateController(controllers, aClass);
        }
        return controllers;
    }

    private static void instantiateController(Map<Class<?>, Object> controllers, Class<?> aClass) {
        try {
            final Constructor<?> constructor = aClass.getDeclaredConstructor();
            controllers.put(aClass, constructor.newInstance());
            log.info("Instantiated Controller - {}", aClass.getCanonicalName());
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            log.error("Instantiating Controller failed! - {}", aClass.getCanonicalName(), e);
            throw new InstantiatingFailedException();
        }
    }
}
