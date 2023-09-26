package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
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
        setFields(beans);
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        final Set<Class<?>> classes = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(classes);
    }

    private void setFields(final Set<Object> beans) {
        for (final Object bean : beans) {
            final Field[] declaredFields = bean.getClass().getDeclaredFields();
            injectBeans(declaredFields, bean);
        }
    }

    private void injectBeans(final Field[] declaredFields, final Object bean) {
        Arrays.stream(declaredFields)
            .filter(field -> filterFieldIsNull(field, bean))
            .filter(field -> field.isAnnotationPresent(Inject.class))
            .forEach(field -> injectBean(field, bean));
    }

    private boolean filterFieldIsNull(final Field field, final Object bean) {
        try {
            field.setAccessible(true);
            return Objects.isNull(field.get(bean));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void injectBean(final Field field, final Object object) {
        try {
            field.set(object, getBean(field.getType()));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<Object> createBeans(final Set<Class<?>> classes) {
        return classes.stream()
            .map(this::createDefaultObject)
            .collect(Collectors.toSet());
    }

    private Object createDefaultObject(final Class<?> clazz) {
        try {
            final Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (final Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> clazz) {
        return (T) beans.stream()
            .filter(object -> clazz.isAssignableFrom(object.getClass()))
            .findFirst()
            .orElse(null);
    }
}
