package di.stage4.annotations;

import java.util.HashSet;
import java.util.Set;
import org.reflections.Reflections;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> classes = new HashSet<>();
        Set<Class<?>> repositoryClasses = reflections.getTypesAnnotatedWith(Repository.class);
        Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> injectClasses = reflections.getTypesAnnotatedWith(Inject.class);
        classes.addAll(repositoryClasses);
        classes.addAll(serviceClasses);
        classes.addAll(injectClasses);
        return classes;
    }
}
