package nextstep.mvc.servlet;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ControllerScanner {
    private final Reflections reflections;

    public ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers(Class<? extends Annotation> annotation) {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(annotation);
        return instantiateControllers(controllers);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllers) {
        return controllers.stream()
                .collect(Collectors.toMap(controller -> controller, this::getNewInstance));
    }

    private Object getNewInstance(Class<?> controller) {
        try {
            return controller.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException("인스턴스 생성에 문제 발생");
        }
    }
}
