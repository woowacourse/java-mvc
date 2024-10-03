package di.stage4.annotations;

import java.util.Set;
import org.reflections.Reflections;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> beanClasses = reflections.getTypesAnnotatedWith(Repository.class);
        beanClasses.addAll(reflections.getTypesAnnotatedWith(Service.class));
        beanClasses.addAll(reflections.getTypesAnnotatedWith(Inject.class));
        return beanClasses;
    }
}
