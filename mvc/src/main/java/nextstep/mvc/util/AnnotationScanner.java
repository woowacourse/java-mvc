package nextstep.mvc.util;

import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

public class AnnotationScanner {

    public static Set<Class<?>> scanClass(Object packagePath, Class<? extends Annotation> annotation) {
        Reflections reflections = new Reflections(packagePath);
        return reflections.getTypesAnnotatedWith(annotation);
    }

    public static Set<Method> scanMethod(Class<?> clazz, Class<? extends Annotation> annotation) {
        return ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(annotation));
    }
}
