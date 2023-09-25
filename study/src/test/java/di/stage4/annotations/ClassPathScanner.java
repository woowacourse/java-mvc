package di.stage4.annotations;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassPathScanner {

    private static final List<Class<? extends Annotation>> targets = List.of(Service.class, Repository.class);

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Reflections reflections = new Reflections(packageName);
        return getAnnotatedClasses(reflections);
    }

    private static Set<Class<?>> getAnnotatedClasses(Reflections reflections) {
        return targets.stream()
                .map(reflections::getTypesAnnotatedWith)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }
}
