package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.stream.Collectors;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        return reflections.getTypesAnnotatedWith(Controller.class)
                .stream()
                .collect(Collectors.toMap(it -> it, this::instantiateController));
    }

    private Object instantiateController(Class<?> clazz) {
        try {
            final var constructor = clazz.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ignored) {
            throw new IllegalArgumentException("해당 Controller의 인스턴스화에 실패하였습니다.");
        }
    }
}
