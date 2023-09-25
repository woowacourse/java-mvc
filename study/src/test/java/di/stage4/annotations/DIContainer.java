package di.stage4.annotations;

import static java.util.stream.Collectors.toSet;

import di.ConsumerWrapper;
import di.FunctionWrapper;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private static final Set<Class<? extends Annotation>> ANNOTATIONS = Set.of(Service.class, Repository.class);

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createBean(classes);
        this.beans.forEach(ConsumerWrapper.accept(this::setField));
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> classes = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(classes);
    }

    private Set<Object> createBean(final Set<Class<?>> classes) {
        return classes.stream()
                .filter(this::isAnnotationPresent)
                .map(FunctionWrapper.apply(this::instantiate))
                .collect(toSet());
    }

    private boolean isAnnotationPresent(final Class<?> type) {
        return ANNOTATIONS.stream()
                .anyMatch(type::isAnnotationPresent);
    }

    private Object instantiate(final Class<?> clazz) throws Exception {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        Object instance = constructor.newInstance();
        constructor.setAccessible(false);
        return instance;
    }

    private void setField(Object bean) throws IllegalAccessException {
        for (final Field field : bean.getClass().getDeclaredFields()) {
            setSingleField(bean, field);
        }
    }

    private void setSingleField(final Object bean, final Field field) throws IllegalAccessException {
        if (field.isAnnotationPresent(Inject.class)) {
            field.setAccessible(true);
            field.set(bean, getBean(field.getType()));
            field.setAccessible(false);
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
