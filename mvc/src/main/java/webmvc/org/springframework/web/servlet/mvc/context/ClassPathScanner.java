package webmvc.org.springframework.web.servlet.mvc.context;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class ClassPathScanner {

    public static Set<Class<?>> getAllAnnotatedClassesInPackage(final List<Class<? extends Annotation>> annotations, final Object... packageName) {
        final var reflections = new Reflections(packageName);
        return annotations.stream()
                          .map(reflections::getTypesAnnotatedWith)
                          .flatMap(Set::stream)
                          .collect(Collectors.toSet());
    }

    public static Set<Class<?>> getAllConcreteClassesInPackage(final List<Class<?>> superClasses, final Object... rootPackageName) {
        final var reflections = new Reflections(rootPackageName);
        return superClasses.stream()
                           .map(reflections::getSubTypesOf)
                           .flatMap(Set::stream)
                           .filter(clazz -> !clazz.isInterface())
                           .collect(Collectors.toSet());
    }
}
