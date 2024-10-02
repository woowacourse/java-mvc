package di.stage4.annotations;

import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        validatePackageName(packageName);

        final Reflections reflections = new Reflections(packageName);
        final Set<Class<?>> result = new HashSet<>();
        final Set<Class<?>> classesWithRepository = reflections.getTypesAnnotatedWith(Repository.class);
        final Set<Class<?>> classesWithService = reflections.getTypesAnnotatedWith(Service.class);
        result.addAll(classesWithRepository);
        result.addAll(classesWithService);

        return result;
    }

    private static void validatePackageName(final String packageName) {
        if (packageName == null || packageName.isBlank()) {
            throw new IllegalArgumentException("패키지명은 null 혹은 공백이 입력될 수 없습니다.");
        }
    }
}
