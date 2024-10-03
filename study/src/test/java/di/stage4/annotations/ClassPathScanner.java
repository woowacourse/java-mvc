package di.stage4.annotations;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.reflections.Reflections;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Reflections reflections = new Reflections(packageName);
        return Stream.of(
                        reflections.getTypesAnnotatedWith(Inject.class).stream(),
                        reflections.getTypesAnnotatedWith(Service.class).stream(),
                        reflections.getTypesAnnotatedWith(Repository.class).stream())
                .flatMap(clazz -> clazz)
                .collect(Collectors.toSet());
    }
}
