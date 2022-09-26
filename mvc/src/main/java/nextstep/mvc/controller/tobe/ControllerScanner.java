package nextstep.mvc.controller.tobe;

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

    private final Reflections reflections;

    public ControllerScanner(final Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        return controllers.stream()
                .collect(Collectors.toMap(Function.identity(), this::instantiate));
    }

    private Object instantiate(final Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (NoSuchMethodException
                 | InstantiationException
                 | InvocationTargetException
                 | IllegalAccessException e) {
            log.error("fail initialize!");
            throw new IllegalStateException(e);
        }
    }
}
