package di.stage4.annotations;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Reflections reflections = new Reflections(packageName);
        HashSet<Class<?>> annotatedClasses = new HashSet<>();
        List<Class<? extends Annotation>> classes = List.of(Repository.class, Service.class);
        for (Class<? extends Annotation> aClass : classes) {
            Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(aClass);
            annotatedClasses.addAll(typesAnnotatedWith);
        }
        return annotatedClasses;
    }
}
