package di.stage4.annotations;

import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
            .forPackage(packageName)
            .addScanners(Scanners.TypesAnnotated));

        Set<Class<?>> injectClasses = reflections.getTypesAnnotatedWith(Inject.class);
        Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositoryClasses = reflections.getTypesAnnotatedWith(Repository.class);

        Set<Class<?>> annotatedClasses = new HashSet<>();
        annotatedClasses.addAll(injectClasses);
        annotatedClasses.addAll(serviceClasses);
        annotatedClasses.addAll(repositoryClasses);

        return annotatedClasses;
    }
}
