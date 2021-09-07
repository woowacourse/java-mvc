package nextstep.mvc;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class ClassScanner {

    private final Reflections reflections;

    public ClassScanner(final Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    public Set<Object> scanAllObjectsWithAnnotation(final Class<? extends Annotation> annotationType) {
        final Set<Class<?>> objectTypes = reflections.getTypesAnnotatedWith(annotationType);

        return objectTypes.stream().map(type -> {
            try {
                return type.getDeclaredConstructor().newInstance();
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new IllegalStateException(String.format("컨트롤러를 인스턴스화 할 수 없습니다.(%s)", type.getName()));
            }
        }).collect(Collectors.toSet());
    }

    public Set<Method> scanAllMethodsWithAnnotation(final Object object, final Class<? extends Annotation> annotationType) {
        final Method[] methods = object.getClass().getDeclaredMethods();

        return Arrays.stream(methods)
            .filter(method -> containsAnnotation(method, annotationType))
            .collect(Collectors.toSet());
    }

    private boolean containsAnnotation(final Method method, final Class<? extends Annotation> annotationType) {
        return Arrays.stream(method.getDeclaredAnnotations())
            .anyMatch(annotation -> annotation.annotationType().equals(annotationType));
    }
}
