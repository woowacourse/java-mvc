package di.stage4.annotations;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public class ClassPathScanner {

    private static final List<Class<? extends Annotation>> BEAN_ANNOTATIONS = List.of(Service.class, Repository.class);

    public static Set<Class<?>> getAllClassesInPackage(String packageName) {
        return getAllClassesInPackage(packageName, BEAN_ANNOTATIONS);
    }

    // @Service, @Repository 애노테이션이 붙은 클래스들을 모두 찾아 반환
    public static Set<Class<?>> getAllClassesInPackage(String packageName, List<Class<? extends Annotation>> annotations) {
        Reflections reflections = new Reflections(packageName, Scanners.TypesAnnotated);
        return BEAN_ANNOTATIONS.stream()
                .map(reflections::getTypesAnnotatedWith)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }
}
