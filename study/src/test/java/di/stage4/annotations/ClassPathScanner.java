package di.stage4.annotations;

import java.util.HashSet;
import java.util.Set;
import org.reflections.Reflections;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);
        HashSet<Class<?>> classes = new HashSet<>(services);
        classes.addAll(repositories);
        return classes;
    }
}
