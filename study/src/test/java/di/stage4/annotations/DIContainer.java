package di.stage4.annotations;

import di.ConsumerWrapper;
import di.FunctionWrapper;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> allClasses = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return allClasses.stream()
                .filter(c -> c.isAnnotationPresent(Service.class) || c.isAnnotationPresent(Repository.class))
                .collect(Collectors.collectingAndThen(Collectors.toUnmodifiableSet(), DIContainer::new));
    }

    private DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        this.beans.forEach(this::setFields);
    }

    private Set<Object> createBeans(Set<Class<?>> classes) {
        return classes.stream()
                .map(FunctionWrapper.apply(this::createBean))
                .collect(Collectors.toUnmodifiableSet());
    }

    private Object createBean(Class<?> aClass) throws Exception {
        Constructor<?> constructor = aClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        Object instance = constructor.newInstance();
        constructor.setAccessible(false);
        return instance;
    }

    private void setFields(Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        Arrays.stream(fields)
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .forEach(ConsumerWrapper.accept(field -> setField(bean, field)));
    }

    private void setField(Object bean, Field field) throws Exception {
        field.setAccessible(true);
        Object fieldInstance = findFieldBean(field);
        field.set(bean, fieldInstance);
        field.setAccessible(false);
    }

    private Object findFieldBean(Field field) {
        return beans.stream()
                .filter(bean -> canAssign(field, bean))
                .findAny()
                .orElse(null);
    }

    private boolean canAssign(Field field, Object bean) {
        return field.getType().isInstance(bean);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(bean -> bean.getClass().equals(aClass))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 빈이 존재하지 않습니다: %s"
                        .formatted(aClass.getSimpleName())));
    }
}
