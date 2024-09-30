package di.stage4.annotations;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class ClassPathScanner {

    private static final List<Class<? extends Annotation>> ANNOTATIONS = List.of(Service.class, Repository.class);

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Reflections reflections = new Reflections(packageName);
        return ANNOTATIONS.stream()
            .flatMap(annotation -> reflections.getTypesAnnotatedWith(annotation).stream())
            .collect(Collectors.toSet());
    }
}
