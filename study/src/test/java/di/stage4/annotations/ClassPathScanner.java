package di.stage4.annotations;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import org.reflections.Reflections;

public class ClassPathScanner {

    private static final Set<Class<? extends Annotation>> TARGET_ANNOTATIONS = Set.of(Service.class, Repository.class);

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Set<Class<?>> classes = new HashSet<>();
        Reflections reflections = new Reflections(packageName);

        for (Class<? extends Annotation> annotation : TARGET_ANNOTATIONS) {
            classes.addAll(reflections.getTypesAnnotatedWith(annotation));
        }

        return classes;
    }
}
