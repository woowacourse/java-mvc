package nextstep.mvc.controller.tobe;

import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

public class ControllerScanner {

    private ControllerScanner() {
    }

    public static Set<Class<?>> scanClassByAnnotation(Object[] basePackage, Class<? extends Annotation> annotation) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(annotation);
    }

    public static Set<Method> scanMethodByClass(Class<?> aClass, Class<? extends Annotation> annotation) {
        return ReflectionUtils.getAllMethods(aClass, ReflectionUtils.withAnnotation(annotation));
    }
}
