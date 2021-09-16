package nextstep.mvc.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    public Map<Class<?>, Object> getControllers(Object[] basePackage) {
        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        log.info("Controller Names: {}", controllers);
        Map<Class<?>, Object> classObjectMap = instantiateControllers(controllers);
        log.info("Controllers: {}", classObjectMap);
        return classObjectMap;
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllers) {
        return controllers.stream()
                .collect(Collectors.toMap(Function.identity(), this::newInstanceOf));
    }

    private Object newInstanceOf(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Handler initializing error");
        }
    }
}
