package di.stage4.annotations;

import java.util.Set;
import org.reflections.Reflections;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        final Set<Class<?>> classes = new Reflections(packageName)
            .getTypesAnnotatedWith(Repository.class);
        classes.addAll(new Reflections(packageName).getTypesAnnotatedWith(Service.class));
        return classes;
    }
}
