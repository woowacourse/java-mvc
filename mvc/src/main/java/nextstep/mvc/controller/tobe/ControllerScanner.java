package nextstep.mvc.controller.tobe;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public class ControllerScanner {

    public Map<Class<?>, Object> getControllers(final Object[] basePackage) {
        Reflections classReflections = new Reflections(basePackage, Scanners.TypesAnnotated);
        Set<Class<?>> classes = classReflections.getTypesAnnotatedWith(Controller.class);

        return instantiateControllers(classes);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> classes) {
        return classes.stream()
                .collect(Collectors.toMap(clazz -> clazz, this::instantiateClass));
    }

    private Object instantiateClass(final Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Controller를 인스턴스화 할 수 없습니다.");
            // TODO: 적절한 예외를 던져야함
        }
    }
}
