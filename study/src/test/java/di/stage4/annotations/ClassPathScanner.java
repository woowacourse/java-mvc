package di.stage4.annotations;

import java.util.HashSet;
import java.util.Set;
import org.reflections.Reflections;

public class ClassPathScanner {

    public static Set<Class<?>> scanForComponents(String packageName) {
        Reflections reflections = new Reflections(packageName);

        Set<Class<?>> components = new HashSet<>();
        components.addAll(reflections.getTypesAnnotatedWith(Service.class));
        components.addAll(reflections.getTypesAnnotatedWith(Repository.class));

        return components;
    }
}
