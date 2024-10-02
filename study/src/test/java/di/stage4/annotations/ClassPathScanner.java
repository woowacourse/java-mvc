package di.stage4.annotations;

import java.util.HashSet;
import java.util.Set;
import org.reflections.Reflections;

public class ClassPathScanner {

    private static final Set<Class> DI_TARGET_CLASSES = Set.of(
            Inject.class,
            Service.class,
            Repository.class
    );

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Set<Class<?>> classes = new HashSet<>();

        Reflections reflections = new Reflections(packageName);
        for (Class targetClass : DI_TARGET_CLASSES) {
            classes.addAll(reflections.getTypesAnnotatedWith(targetClass));
        }
        return classes;
    }
}
