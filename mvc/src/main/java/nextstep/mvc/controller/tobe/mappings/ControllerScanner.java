package nextstep.mvc.controller.tobe.mappings;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    public Map<Class<?>, Object> createInstanceByClasses(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        return classes.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        this::createInstance
                ));
    }

    private Object createInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException |
                 IllegalAccessException |
                 InvocationTargetException |
                 NoSuchMethodException e) {
            throw new IllegalArgumentException("인스턴스 생성 시 오류가 발생했습니다", e);
        }
    }
}
