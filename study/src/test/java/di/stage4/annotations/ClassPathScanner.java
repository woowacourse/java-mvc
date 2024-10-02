package di.stage4.annotations;

import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> injectClasses = reflections.getTypesAnnotatedWith(Inject.class);
        Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositoryClasses = reflections.getTypesAnnotatedWith(Repository.class);

        return Set.of(injectClasses, serviceClasses, repositoryClasses).stream()
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }
}
