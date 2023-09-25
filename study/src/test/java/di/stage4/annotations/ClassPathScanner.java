package di.stage4.annotations;

import org.reflections.Reflections;

import java.util.Set;
import java.util.stream.Collectors;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Component.class);
        return classes.stream()
                .filter(clazz -> !clazz.isAnnotation())
                .collect(Collectors.toSet());
    }
}
