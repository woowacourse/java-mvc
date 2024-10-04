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

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        this.beans.forEach(this::initFields);
    }

    private Set<Object> createBeans(final Set<Class<?>> classes) {
        return classes.stream()
                .map(FunctionWrapper.apply(this::instantiateClass))
                .collect(Collectors.toUnmodifiableSet());
    }

    private Object instantiateClass(final Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object instance = constructor.newInstance();
            constructor.setAccessible(false);
            return instance;
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("인스턴스화 할 수 없습니다.");
        }
    }

    private void initFields(final Object bean) {
        final Field[] fields = bean.getClass().getDeclaredFields();
        Arrays.stream(fields)
                .forEach(ConsumerWrapper.accept(field -> initField(bean, field)));
    }

    private void initField(Object bean, Field field) {
        field.setAccessible(true);
        final Class<?> fieldType = field.getType();
        beans.stream()
                .filter(o -> fieldType.isAssignableFrom(o.getClass()))
                .forEach(ConsumerWrapper.accept(o -> field.set(bean, o)));
        field.setAccessible(false);
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> classes = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return classes.stream()
                .filter(c -> c.isAnnotationPresent(Repository.class) || c.isAnnotationPresent(Service.class))
                .collect(Collectors.collectingAndThen(Collectors.toUnmodifiableSet(), DIContainer::new));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(bean -> aClass.isAssignableFrom(bean.getClass()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 빈입니다."));
    }
}
