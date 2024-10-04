package di.stage4.annotations;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;

public class ClassPathScanner {

    private static Class<?>[] targetAnnotations = {Service.class, Repository.class};

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        HashSet<Class<?>> classes = new HashSet<>();
        Reflections reflections = new Reflections(packageName);
        for (Class<?> targetAnnotation : targetAnnotations) {
            classes.addAll(reflections.getTypesAnnotatedWith((Class<? extends Annotation>)targetAnnotation));
        }
        return classes;
    }
}
