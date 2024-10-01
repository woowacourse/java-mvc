package di.stage4.annotations;

import org.reflections.Reflections;

import java.util.Set;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Service.class);
        classes.addAll(reflections.getTypesAnnotatedWith(Repository.class));
        return classes;
    }
}
