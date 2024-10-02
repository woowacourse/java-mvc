package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.function.Consumer;
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
        return classes.stream()
                .filter(clazz -> clazz.isAnnotationPresent(Service.class) || clazz.isAnnotationPresent(Repository.class))
                .collect(Collectors.collectingAndThen(Collectors.toUnmodifiableSet(), DIContainer::new));
    }

    private Set<Object> createBeans(final Set<Class<?>> classes) {
        return classes.stream()
                .map(this::createInstance)
                .collect(Collectors.toSet());
    }

    private Object createInstance(final Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void setFields(final Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            setField(bean, field);
        }
    }

    private void setField(final Object bean, final Field field) {
        Class<?> fieldType = field.getType();
        field.setAccessible(true);

        beans.stream()
                .filter(fieldType::isInstance)
                .forEach(getObjectConsumer(bean, field));
    }

    private Consumer<Object> getObjectConsumer(Object bean, Field field) {
        return matchBean -> {
            try {
                field.set(bean, matchBean);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(aClass::isInstance)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
