package di.stage4.annotations;

import java.util.HashSet;
import java.util.Set;
import org.reflections.Reflections;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        final Reflections reflections = new Reflections(packageName);
        final Set<Class<?>> response = new HashSet<>(reflections.getTypesAnnotatedWith(Inject.class));
        response.addAll(reflections.getTypesAnnotatedWith(Service.class));
        response.addAll(reflections.getTypesAnnotatedWith(Repository.class));

        return response;
    }
}
