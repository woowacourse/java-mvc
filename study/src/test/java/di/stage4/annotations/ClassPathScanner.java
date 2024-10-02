package di.stage4.annotations;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        final Reflections reflections = new Reflections(packageName);
        Set<Class<?>> foundClass = new HashSet<>();

        foundClass.addAll(getAnnotatedClass(Inject.class, reflections));
        foundClass.addAll(getAnnotatedClass(Service.class, reflections));
        foundClass.addAll(getAnnotatedClass(Repository.class, reflections));

        return foundClass;
    }

    private static Set<Class<?>> getAnnotatedClass(Class<? extends Annotation> annotationClass, final Reflections reflections) {
        return reflections.getTypesAnnotatedWith(annotationClass);
    }
}
