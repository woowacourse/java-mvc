package di.stage4.annotations;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Reflections reflections = new Reflections(packageName);
        return reflections.getSubTypesOf(Object.class);
    }

    public static Set<Class<?>> getAnnotatedClassesInPackage(
            final String packageName,
            final Class<? extends Annotation> annotation
    ) {
        Reflections reflections = new Reflections(packageName, Scanners.TypesAnnotated);
        return reflections.getTypesAnnotatedWith(annotation);
    }

    public static Set<Class<?>> getAnnotatedClassesInPackage(
            final String packageName,
            final Class<? extends Annotation>... annotations
    ) {
        Reflections reflections = new Reflections(packageName, Scanners.TypesAnnotated);
        return Arrays.stream(annotations)
                .flatMap(annotation -> reflections.getTypesAnnotatedWith(annotation).stream())
                .collect(Collectors.toSet());
    }
}
