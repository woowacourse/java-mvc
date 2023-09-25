package di.stage4.annotations;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.platform.commons.util.ClassFilter;
import org.junit.platform.commons.util.ReflectionUtils;

public class ClassPathScanner {

    public static Set<Class<?>> getAllAnnotatedClassesInPackage(final String packageName, final List<Class<? extends Annotation>> annotation) {
        final var allClassesInPackage = ReflectionUtils.findAllClassesInPackage(packageName, ClassFilter.of(clazz -> !clazz.isInterface()));
        return allClassesInPackage.stream()
                                  .filter(clazz -> isAnnotatedWith(clazz, annotation))
                                  .collect(Collectors.toSet());
    }

    private static boolean isAnnotatedWith(Class<?> clazz, List<Class<? extends Annotation>> annotations) {
        return annotations.stream()
                          .anyMatch(clazz::isAnnotationPresent);
    }
}
