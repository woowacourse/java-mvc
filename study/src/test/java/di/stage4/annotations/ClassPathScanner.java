package di.stage4.annotations;

import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class ClassPathScanner {

    private ClassPathScanner() {
    }

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Reflections reflections = new Reflections(packageName);

        return Stream.of(Service.class, Repository.class)
                .map(reflections::getTypesAnnotatedWith)
                .collect(HashSet::new, Set::addAll, Set::addAll);
    }

}
