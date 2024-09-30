package di.stage3.context;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        this.beans.forEach(this::setFields);
    }

    private Set<Object> createBeans(final Set<Class<?>> classes) {
        return classes.stream()
                .map(this::getInstance)
                .collect(Collectors.toSet());
    }

    private Object getInstance(final Class<?> clazz) {
        try {
            final var constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);

            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setFields(final Object bean) {
        List<Field> fields = List.of(bean.getClass().getDeclaredFields());
        fields.forEach(field -> setField(bean, field));
    }

    private void setField(final Object bean, final Field field) {
        Object dependency = findBean(field.getType());
        if (Objects.isNull(dependency)) {
            return;
        }

        field.setAccessible(true);
        try {
            field.set(bean, dependency);
        } catch (IllegalAccessException e) {
            throw new RuntimeException();
        }
    }

    private Object findBean(Class<?> fieldType) {
        return beans.stream()
                .filter(bean -> fieldType.isAssignableFrom(bean.getClass()))
                .findFirst()
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(bean -> aClass.isAssignableFrom(bean.getClass()))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
