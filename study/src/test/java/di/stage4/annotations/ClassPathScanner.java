package di.stage4.annotations;

import org.reflections.Reflections;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Set<Class<?>> classes = new HashSet<>();
        classes.addAll(getServiceInstances(packageName));
        classes.addAll(getRepositoryInstances(packageName));

        return classes;
    }

    private static Set<Class<?>> getInstances(String packageName, Class<? extends Annotation> annotation) {
        Reflections reflections = new Reflections(packageName);
        return reflections.getTypesAnnotatedWith(annotation);
    }

    private static Set<Class<?>> getServiceInstances(String packageName) {
        return getInstances(packageName, Service.class);
    }

    private static Set<Class<?>> getRepositoryInstances(String packageName) {
        return getInstances(packageName, Repository.class);
    }
}
