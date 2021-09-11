package nextstep.mvc.controller.tobe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectionUtils {

    public static <T extends Annotation> List<Method> getMethodsWithAnnotation(Class<?> clazz,
                                                                               Class<T> annotationClass) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotationClass))
                .collect(Collectors.toList());
    }
}
