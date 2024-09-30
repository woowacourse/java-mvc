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
        this.beans = classes.stream()
                .map(FunctionWrapper.apply(Class::getDeclaredConstructor))
                .peek(constructor -> constructor.setAccessible(true))
                .map(FunctionWrapper.apply(Constructor::newInstance))
                .collect(Collectors.toSet());

        this.beans.forEach(this::setFields);
    }

    private void setFields(Object bean) {
        Arrays.stream(bean.getClass().getDeclaredFields())
                .forEach(field -> setField(bean, field));
    }

    private void setField(Object bean, Field field) {
        Class<?> fieldType = field.getType();
        field.setAccessible(true);
        beans.stream()
                .filter(fieldType::isInstance)
                .forEach(ConsumerWrapper.accept(fieldBean -> field.set(bean, fieldBean)));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        return (T) beans.stream()
                .filter(bean -> bean.getClass().isAssignableFrom(clazz))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Bean Not found"));
    }
}
