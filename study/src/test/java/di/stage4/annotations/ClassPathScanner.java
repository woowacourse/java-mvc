package di.stage4.annotations;

import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        return new Reflections(packageName, Scanners.SubTypes.filterResultsBy(filter -> true))
                .getSubTypesOf(Object.class);
    }
}
