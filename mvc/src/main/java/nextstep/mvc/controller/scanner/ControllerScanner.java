package nextstep.mvc.controller.scanner;

import static java.util.stream.Collectors.toMap;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private static ControllerScanner INSTANCE = new ControllerScanner();

    private ControllerScanner() {
    }

    public static ControllerScanner getInstance() {
        return INSTANCE;
    }

    public Map<Class<?>, Object> getControllers(final Object... basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> mappedControllers = reflections.getTypesAnnotatedWith(Controller.class);

        return mappedControllers.stream()
            .collect(toMap(Function.identity(), this::getInitializedController));
    }

    private Object getInitializedController(final Class<?> mappedController) {
        try {
            return mappedController.getConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
