package di.stage4.annotations;

import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .addScanners(Scanners.SubTypes.filterResultsBy(filter -> true))
                .forPackages(packageName));
        return reflections.getSubTypesOf(Object.class);
    }
}
