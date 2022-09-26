package nextstep.mvc.controller;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextstep.web.annotation.Controller;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private ControllerScanner() {
    }

    public static Map<Class<?>, Object> getControllers(final Object... basePackage) {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        return controllers.stream()
            .collect(Collectors.toMap(
                Function.identity(),
                ControllerScanner::initController
            ));
    }

    private static Object initController(final Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructors()[0]
                .newInstance();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(String.format("생성자가 존재하지 않습니다. [%s]", clazz.getName()));
        }
    }
}
