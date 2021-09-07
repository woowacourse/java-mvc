package nextstep.mvc.utils;

import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class AnnotationScanner {

    public static Set<Class<?>> scanClassWith(Object[] basePackages, Class<? extends Annotation> annotation) {
        Set<Class<?>> classes = new HashSet<>();
        Reflections reflections;
        for (Object basePackage : basePackages) {
            reflections = new Reflections(basePackage);
            classes.addAll(reflections.getTypesAnnotatedWith(annotation));
        }

        return classes;
    }

    public static Set<Method> scanMethod(Class clazz, Class<? extends Annotation> annotation) {
       return ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(annotation));
    }
}
