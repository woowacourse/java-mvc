package nextstep.mvc.controller.tobe;

import static org.reflections.Reflections.log;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    public List<Object> getControllers() {
        return reflections.getTypesAnnotatedWith(Controller.class).stream()
                .map(this::createController)
                .collect(Collectors.toList());
    }

    private Object createController(final Class<?> aClass) {
        try {
            return aClass.getConstructor()
                    .newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException exception) {
            log.warn(exception.getMessage());
            throw new IllegalStateException();
        }
    }
}
