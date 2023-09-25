package di.stage4.annotations;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> classes = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(classes);
    }

    private Set<Object> createBeans(final Set<Class<?>> classes) {
        return classes.stream()
                .map(this::newInstanceFromDefaultConstructor)
                .collect(Collectors.toSet());
    }

    private Object newInstanceFromDefaultConstructor(Class<?> clazz) {
        try {
            final var constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void setFields(final Object bean) {
        final var fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            setFieldWithBean(bean, field);
        }
    }

    private void setFieldWithBean(Object bean, Field field) {
        try {
            field.setAccessible(true);
            if (field.get(bean) != null) {
                return;
            }
            Object o = getBean(field);
            field.set(bean, o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getBean(Field field) {
        return beans.stream()
                .filter(b -> field.getType().isInstance(b))
                .findAny()
                .orElseThrow();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(bean -> bean.getClass() == aClass)
                .findAny()
                .orElseThrow();
    }
}
