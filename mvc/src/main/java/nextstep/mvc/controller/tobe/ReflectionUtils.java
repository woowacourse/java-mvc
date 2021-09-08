package nextstep.mvc.controller.tobe;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class ReflectionUtils {

    private Reflections reflections;

    public ReflectionUtils(Object basePackage) {
        this.reflections = new Reflections((String) basePackage);
    }

    public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        return reflections.getTypesAnnotatedWith(annotation);
    }

    public Object newInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
    }

    public List<Method> methods(Class<?> annotatedClass, Class<? extends Annotation> annotation) {
        return Arrays.stream(annotatedClass.getMethods())
            .filter(value -> value.isAnnotationPresent(annotation))
            .collect(Collectors.toList());
    }
}
