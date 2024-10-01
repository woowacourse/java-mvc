package di.stage4.annotations;

import java.util.HashSet;
import java.util.Set;
import org.reflections.Reflections;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> allClasses = new HashSet<>();
        allClasses.addAll(reflections.getTypesAnnotatedWith(Service.class));
        allClasses.addAll(reflections.getTypesAnnotatedWith(Repository.class));
        return allClasses;
    }
}
