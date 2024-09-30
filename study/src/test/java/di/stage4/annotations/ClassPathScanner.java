package di.stage4.annotations;

import java.util.HashSet;
import java.util.Set;
import org.reflections.Reflections;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositoryClasses = reflections.getTypesAnnotatedWith(Repository.class);
        Set<Class<?>> allClasses = new HashSet<>(serviceClasses);
        allClasses.addAll(repositoryClasses);
        return allClasses;
    }
}
