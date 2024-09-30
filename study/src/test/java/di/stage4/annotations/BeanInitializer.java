package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BeanInitializer {

    private final Map<Class<?>, Object> instanceRegistry = new HashMap<>();

    public void initialize(final String basePackage) throws Exception {
        final Set<Class<?>> allClassesInPackage = ClassPathScanner.getAllClassesInPackage(basePackage);
        for (final Class<?> clazz : allClassesInPackage) {
            final Constructor<?> noArgsConstructor = clazz.getDeclaredConstructor();
            noArgsConstructor.setAccessible(true);
            final Object instance = noArgsConstructor.newInstance();
            instanceRegistry.put(clazz, instance);
        }
    }

    public void injectDependency() {
        final Collection<Object> values = instanceRegistry.values();
        for (Object instance : values) {
            final Field[] fields = instance.getClass().getDeclaredFields();
            Arrays.stream(fields)
                    .filter(field -> field.isAnnotationPresent(Inject.class))
                    .forEach(field -> inject(instance, field));
        }
    }

    private void inject(final Object instance, final Field field) {
        try {
            field.setAccessible(true);
            final Object dependency = instanceRegistry.values()
                    .stream()
                    .filter(obj -> field.getType().isInstance(obj))
                    .findAny()
                    .orElseThrow();
            field.set(instance, dependency);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Object> getInstance() {
        return new HashSet<>(instanceRegistry.values());
    }
}
