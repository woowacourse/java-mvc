package di.stage4.annotations;

import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Set;

public class ClassPathScanner {

    private static Set<Class<?>> classes = new HashSet<>();

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Reflections reflections = new Reflections(packageName);
        addServiceClasses(reflections);
        addRepositoryClasses(reflections);
        return classes;
    }

    private static void addServiceClasses(Reflections reflections) {
        classes.addAll(reflections.getTypesAnnotatedWith(Service.class));
    }

    private static void addRepositoryClasses(Reflections reflections) {
        classes.addAll(reflections.getTypesAnnotatedWith(Repository.class));
    }
}
