package di.stage3.context;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Class<?>> classes;
    private final Map<Class<?>, Object> beans = new HashMap<>();

    public DIContainer(final Set<Class<?>> classes) {
        this.classes = classes;
        classes.forEach(this::createBean);
    }

    public <T> T getBean(final Class<T> aClass) {
        return classes.stream()
                .filter(aClass::isAssignableFrom)
                .findFirst()
                .map(this::createBean)
                .map(aClass::cast)
                .orElseThrow(() -> new IllegalArgumentException("No bean for " + aClass.getName()));
    }

    private Object createBean(final Class<?> beanClass) {
        if (beans.containsKey(beanClass)) {
            return beans.get(beanClass);
        }

        try {
            final Constructor<?> constructor = Arrays.stream(beanClass.getDeclaredConstructors())
                    .max(Comparator.comparingInt(Constructor::getParameterCount))
                    .orElseThrow();
            constructor.setAccessible(true);
            final Object[] arguments = Arrays.stream(constructor.getParameterTypes())
                    .map(this::getBean)
                    .toArray();
            final Object bean = constructor.newInstance(arguments);
            beans.put(beanClass, bean);
            return bean;
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Failed to create bean: " + beanClass.getName(), exception);
        }
    }
}
