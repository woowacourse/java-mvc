package di.stage4.annotations;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.util.Set;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        final var reflections = new Reflections(
                new ConfigurationBuilder()
                        .forPackage(packageName)
                        .setScanners(Scanners.SubTypes.filterResultsBy(filter -> true))
        );
        return reflections.getSubTypesOf(Object.class);
    }
}
