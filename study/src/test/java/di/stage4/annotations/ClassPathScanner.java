package di.stage4.annotations;

import java.util.HashSet;
import java.util.Set;
import org.reflections.Reflections;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Reflections reflections = new Reflections(packageName);

        Set<Class<?>> repositoryAnnotated = reflections.getTypesAnnotatedWith(Repository.class);
        Set<Class<?>> serviceAnnotated = reflections.getTypesAnnotatedWith(Service.class);

        Set<Class<?>> result = new HashSet<>();
        result.addAll(repositoryAnnotated);
        result.addAll(serviceAnnotated);

        return result;
    }
}
