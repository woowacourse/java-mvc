package di.stage4.annotations;

import java.util.Set;
import java.util.stream.Collectors;
import org.junit.platform.commons.support.ReflectionSupport;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        return ReflectionSupport.streamAllClassesInPackage(packageName, clazz -> true, name -> true)
                .collect(Collectors.toSet());
    }
}
