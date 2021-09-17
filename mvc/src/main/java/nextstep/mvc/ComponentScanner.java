package nextstep.mvc;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ComponentScanner {

    private final Reflections reflections;

    public ComponentScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<String, Object> getComponent(Class<? extends Annotation> annotation) {
        return findComponentWithName(reflections.getTypesAnnotatedWith(annotation));
    }

    private Map<String, Object> findComponentWithName(Set<Class<?>> componentClass) {
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
