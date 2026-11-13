package di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.ReflectiveOperationException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        final Map<Class<?>, Object> instances = new HashMap<>();
        for (final Class<?> clazz : classes) {
            createBean(clazz, classes, instances);
        }
        this.beans = Set.copyOf(instances.values());
    }

    public <T> T getBean(final Class<T> aClass) {
        return beans.stream()
                .filter(aClass::isInstance)
                .map(aClass::cast)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No bean found: " + aClass.getName()));
    }

    private Object createBean(final Class<?> clazz, final Set<Class<?>> classes,
                              final Map<Class<?>, Object> instances) {
        final Object existing = instances.get(clazz);
        if (existing != null) {
            return existing;
        }

        try {
            final Constructor<?> constructor = Arrays.stream(clazz.getDeclaredConstructors())
                    .max(Comparator.comparingInt(Constructor::getParameterCount))
                    .orElseThrow();
            constructor.setAccessible(true);
            final Object[] arguments = Arrays.stream(constructor.getParameterTypes())
                    .map(parameterType -> classes.stream()
                            .filter(parameterType::isAssignableFrom)
                            .findFirst()
                            .map(dependency -> createBean(dependency, classes, instances))
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "No bean found: " + parameterType.getName())))
                    .toArray();
            final Object bean = constructor.newInstance(arguments);
            instances.put(clazz, bean);
            return bean;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot create bean: " + clazz.getName(), e);
        }
    }
}
