package di.stage3.context;

import static java.util.stream.Collectors.toSet;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createBean(classes);
        this.beans.forEach(this::setField);
    }

    private Set<Object> createBean(final Set<Class<?>> classes) {
        return classes.stream()
                .map(this::instantiate)
                .collect(toSet());
    }

    private Object instantiate(final Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object instance = constructor.newInstance();
            constructor.setAccessible(false);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void setField(Object bean) {
        for (final Field field : bean.getClass().getDeclaredFields()) {
            setSingleField(bean, field);
        }
    }

    private void setSingleField(final Object bean, final Field field) {
        Object object = getBean(field.getType());
        if (object == null) {
            return;
        }
        try {
            field.setAccessible(true);
            field.set(bean, object);
            field.setAccessible(false);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> type) {
        return (T) beans.stream()
                .filter(bean -> type.isAssignableFrom(bean.getClass()))
                .findFirst()
                .orElse(null);
    }
}
