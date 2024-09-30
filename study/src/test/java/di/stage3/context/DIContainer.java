package di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        Set<Object> beans = new HashSet<>();
        for (Class<?> clazz : classes) {
            beans.add(getInstance(clazz, classes));
        }

        this.beans = beans;
    }

    private Object getInstance(Class<?> clazz, Set<Class<?>> classes) {
        Class<?>[] parameterClasses = Arrays.stream(clazz.getDeclaredFields())
                .map(Field::getType)
                .filter(type -> classes.stream()
                            .anyMatch(type::isAssignableFrom))
                .toArray(Class[]::new);

        try {
            Class<?> constructClass = classes.stream()
                    .filter(clazz::isAssignableFrom)
                    .findFirst()
                    .orElseThrow();
            Constructor<?> constructor= constructClass.getConstructor(parameterClasses);

            Object[] parameterInstances = Arrays.stream(parameterClasses)
                    .map(pc -> getInstance(pc, classes)).toArray();

            return constructor.newInstance(parameterInstances);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(instance -> instance.getClass().equals(aClass))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No beans exist for " + aClass.getName() +" in this container"));
    }
}
