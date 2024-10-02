package di.stage3.context;

import di.ConsumerWrapper;
import di.FunctionWrapper;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = classes.stream()
                .map(FunctionWrapper.apply(Class::getDeclaredConstructor))
                .peek(constructor -> constructor.setAccessible(true))
                .map(FunctionWrapper.apply(Constructor::newInstance))
                .collect(Collectors.toUnmodifiableSet());
        setFiled(beans);
    }

    private void setFiled(Set<Object> beans) {
        for (Object bean : beans) {
            for (Field field : bean.getClass().getDeclaredFields()) {
                Class<?> type = field.getType();
                field.setAccessible(true);

                beans.stream()
                        .filter(type::isInstance)
                        .forEach(ConsumerWrapper.accept(b -> field.set(bean, b)));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return beans.stream()
                .filter(aClass::isInstance)
                .findFirst()
                .map(aClass::cast)
                .orElseThrow();
    }
}
