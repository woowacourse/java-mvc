package di.stage4.annotations;

import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Scanners scanners = Scanners.SubTypes.filterResultsBy(filter -> true);
        Reflections reflections = new Reflections(packageName, scanners);

        return reflections.getSubTypesOf(Object.class);
    }
}
