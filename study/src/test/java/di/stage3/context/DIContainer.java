package di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private static final Logger log = LoggerFactory.getLogger(DIContainer.class);
    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.beans = new HashSet<>();

        for (Class<?> clazz : classes) {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            beans.add(constructor.newInstance());
        }

        beans.forEach(this::injectDependencies);
    }

    private void injectDependencies(Object instance) {
        Arrays.stream(instance.getClass().getDeclaredFields())
                .forEach(field -> {
                    field.setAccessible(true);
                    setBeanIntoInstance(instance, field);
                });
    }

    private void setBeanIntoInstance(Object instance, Field field) {
        beans.stream()
                .filter(bean -> field.getType().isAssignableFrom(bean.getClass()))
                .findFirst()
                .ifPresent(bean -> setBeanIntoField(instance, field, bean));
    }

    private void setBeanIntoField(Object instance, Field field, Object bean) {
        try {
            field.set(instance, bean);
        } catch (IllegalAccessException e) {
            log.error("Failed to inject dependency for field: " + field.getName(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(aClass::isInstance)
                .findFirst()
                .orElse(null);
    }
}
