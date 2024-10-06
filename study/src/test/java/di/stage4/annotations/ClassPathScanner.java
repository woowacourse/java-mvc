package di.stage4.annotations;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName,
                                                       final List<Class<? extends Annotation>> targets) {
        final Reflections reflections = new Reflections(packageName);
        return targets.stream()
                .flatMap(target -> reflections.getTypesAnnotatedWith(target).stream())
                .collect(Collectors.toSet());
    }
}
