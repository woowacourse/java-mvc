package nextstep.mvc.handlerMapping;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;

import nextstep.web.annotation.Controller;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllers);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllers) {
        return controllers.stream()
            .collect(Collectors.toUnmodifiableMap(
                key -> key,
                value -> generateInstance(value)
            ));
    }

    private Object generateInstance(final Class<?> controller) {
        try {
            return controller.getConstructor().newInstance();
        } catch (NoSuchMethodException |
                 InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("컨트롤러 생성 과정에서 예외가 발생했습니다.");
        }
    }
}
