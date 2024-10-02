package di.stage4.annotations;

import org.reflections.Reflections;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> classes = new HashSet<>();

        List<Class<? extends Annotation>> annotations = List.of(Service.class, Repository.class);

        for (Class<? extends Annotation> annotation : annotations) {
            classes.addAll(reflections.getTypesAnnotatedWith(annotation));
        }

        return classes;
    }
}
