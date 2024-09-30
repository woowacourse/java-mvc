package di.stage4.annotations;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName, final Class<? extends Annotation>... annotations) {
        final Reflections reflections = new Reflections(packageName);
        return Stream.of(annotations)
                .flatMap(annotation -> reflections.getTypesAnnotatedWith(annotation)
                        .stream())
                .collect(Collectors.toSet());
    }
}
