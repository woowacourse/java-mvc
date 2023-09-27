package di.stage4.annotations;

import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Set;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        final HashSet<Class<?>> classes = new HashSet<>();
        final Reflections reflections = new Reflections(packageName);
        classes.addAll(reflections.getTypesAnnotatedWith(Repository.class));
        classes.addAll(reflections.getTypesAnnotatedWith(Service.class));
        classes.addAll(reflections.getTypesAnnotatedWith(Inject.class));
        return classes;
    }
}
