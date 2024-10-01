package di.stage4.annotations;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class ClassPathScanner {

    private static final List<Class<? extends Annotation>> SCAN_TARGETS = List.of(
            Service.class,
            Repository.class,
            Inject.class
    );

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Reflections reflections = new Reflections(packageName);

        return SCAN_TARGETS.stream()
                .flatMap(target -> reflections.getTypesAnnotatedWith(target).stream())
                .collect(Collectors.toSet());
    }
}
