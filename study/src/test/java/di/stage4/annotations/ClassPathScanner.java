package di.stage4.annotations;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import org.reflections.Reflections;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Set<Class<?>> classes = new HashSet<>();
        Reflections reflections = new Reflections(packageName);

        classes.addAll(reflections.getTypesAnnotatedWith(Service.class));
        classes.addAll(reflections.getTypesAnnotatedWith(Repository.class));

        return classes;
    }

    private static Set<Class<? extends Annotation>> scanAnnotationClasses() {
        return Set.of(Inject.class, Service.class, Repository.class);
    }
}
