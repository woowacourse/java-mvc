package di.stage3.context;

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

    public DIContainer(Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        this.beans.forEach(this::assignFields);
    }

    private Set<Object> createBeans(Set<Class<?>> classes) {
        return classes.stream()
                .map(FunctionWrapper.apply(Class::getDeclaredConstructor))
                .peek(constructor -> constructor.setAccessible(true))
                .map(FunctionWrapper.apply(Constructor::newInstance))
                .collect(Collectors.toUnmodifiableSet());
    }

    private void assignFields(Object bean) {
        Arrays.stream(bean.getClass().getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .forEach(field -> assignField(field, bean));
    }

    private void assignField(Field field, Object bean) {
        beans.stream()
                .filter(field.getType()::isInstance)
                .forEach(ConsumerWrapper.accept(fieldBean -> field.set(bean, fieldBean)));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(aClass::isInstance)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("bean을 찾을 수 없습니다."));
    }
}
