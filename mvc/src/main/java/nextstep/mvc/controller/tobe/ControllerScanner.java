package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.common.exception.ErrorType;
import nextstep.mvc.common.exception.FailedInstantiateObjectException;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        return controllers.stream()
                .collect(Collectors.toMap(controller -> controller, this::instantiateControllers));
    }

    private Object instantiateControllers(final Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new FailedInstantiateObjectException(ErrorType.FAIL_INSTANTIATE_CONTROLLER);
        }
    }
}
