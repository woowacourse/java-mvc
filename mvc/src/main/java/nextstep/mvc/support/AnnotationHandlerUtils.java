package nextstep.mvc.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class AnnotationHandlerUtils {

    private AnnotationHandlerUtils() {
    }

    public static Set<Class<?>> getClassesAnnotatedWith(String basePath, Class<? extends Annotation> annotation) {
        return new Reflections(basePath).getTypesAnnotatedWith(annotation);
    }

    public static List<Method> getMethodsAnnotatedWith(Class<?> type, Class<? extends Annotation> annotation) {
        final List<Method> methods = new ArrayList<>();
        Class<?> klass = type;
        while (klass != Object.class) {
            List<Method> annotatedMethodsInClass = getMethodsAnnotatedWithInClass(klass, annotation);
            methods.addAll(annotatedMethodsInClass);
            klass = klass.getSuperclass();
        }
        return methods;
    }

    private static List<Method> getMethodsAnnotatedWithInClass(Class<?> klass, Class<? extends Annotation> annotation) {
        return Arrays.stream(klass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation))
                .collect(Collectors.toList());
    }
}
