package webmvc.org.springframework.web.servlet.mvc.tobe;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AnnotationScanner {

    private final Reflections reflections;

    public AnnotationScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> findAnnotatedClassWithInstance(Class<? extends Annotation> annotation) {
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(annotation);
        return classByInstance(classes);
    }

    private Map<Class<?>, Object> classByInstance(Set<Class<?>> classes) {
        return classes.stream()
                .collect(Collectors.toMap(Function.identity(), this::newInstance));
    }

    private Object newInstance(Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (
                InstantiationException |
                IllegalAccessException |
                InvocationTargetException |
                NoSuchMethodException e
        ) {
            throw new RuntimeException(e);
        }
    }
}
