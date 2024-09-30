package di.stage4.annotations;

import java.util.Set;

import org.reflections.Reflections;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        final Reflections reflections = new Reflections(packageName);
        final Set<Class<?>> all = reflections.getTypesAnnotatedWith(Service.class);
        final Set<Class<?>> repos = reflections.getTypesAnnotatedWith(Repository.class);
        all.addAll(repos);
        return all;
    }
}
