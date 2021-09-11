package nextstep.mvc;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ComponentScanner {

    public static Map<String, Object> getComponent(
        Object[] basePackage, Class<? extends Annotation> annotation
    ) {
        Reflections reflections = new Reflections(basePackage);
        return findComponentWithName(reflections, annotation);
    }

    private static Map<String, Object> findComponentWithName(
        Reflections reflections, Class<? extends Annotation> annotation
    ) {
        Set<Class<?>> componentClass = reflections.getTypesAnnotatedWith(annotation);
        Map<String, Object> componentInstances = new HashMap<>();

        try {
            for (Class<?> aClass : componentClass) {
                componentInstances.put(
                    aClass.getSimpleName(),
                    aClass.getDeclaredConstructor().newInstance()
                );
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        return componentInstances;
    }
}
