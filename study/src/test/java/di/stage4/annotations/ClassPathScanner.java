package di.stage4.annotations;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.platform.commons.util.ClassFilter;
import org.junit.platform.commons.util.ReflectionUtils;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(
            final String packageName, final List<Class<? extends Annotation>> annotations) {
        return new HashSet<>(ReflectionUtils.findAllClassesInPackage(
                packageName,
                ClassFilter.of(clazz -> isAnnotationPresent(clazz, annotations))
        ));
    }

    private static boolean isAnnotationPresent(
            final Class<?> clazz,
            final List<Class<? extends Annotation>> annotations) {
        return annotations.stream()
                .anyMatch(clazz::isAnnotationPresent);
    }
}
