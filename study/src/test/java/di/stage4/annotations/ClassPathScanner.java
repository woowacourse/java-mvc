package di.stage4.annotations;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class ClassPathScanner {

    private static final Set<Class<? extends Annotation>> annotations =
            Set.of(Repository.class, Service.class, Inject.class);

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Reflections reflections = new Reflections(packageName);

        return annotations.stream()
                          .map(reflections::getTypesAnnotatedWith)
                          .flatMap(Set::stream)
                          .collect(Collectors.toSet());
    }
}
