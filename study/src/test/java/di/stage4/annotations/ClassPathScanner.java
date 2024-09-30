package di.stage4.annotations;

import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Reflections reflections = new Reflections(packageName);

        Set<Class<?>> annotatedClasses = new HashSet<>();

        // 주석이 달린 타입 추가
        annotatedClasses.addAll(reflections.getTypesAnnotatedWith(Inject.class));
        annotatedClasses.addAll(reflections.getTypesAnnotatedWith(Service.class));
        annotatedClasses.addAll(reflections.getTypesAnnotatedWith(Repository.class));

        return annotatedClasses;
    }
}
