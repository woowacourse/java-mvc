package di.stage4.annotations;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public class ClassPathScanner {

    private static final List<Class<? extends Annotation>> BEAN_ANNOTATIONS = List.of(Service.class, Repository.class);

    public static Set<Class<?>> getAllClassesInPackage(String packageName) {
        return getAllClassesInPackage(packageName, BEAN_ANNOTATIONS);
    }

    public static Set<Class<?>> getAllClassesInPackage(String packageName, List<Class<? extends Annotation>> annotations) {
        Reflections reflections = new Reflections(packageName, Scanners.TypesAnnotated);
        return annotations.stream()
                .map(reflections::getTypesAnnotatedWith)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }
}
