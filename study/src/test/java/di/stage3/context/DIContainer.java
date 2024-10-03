package di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = classes.stream()
                .map(this::createBean)
                .collect(Collectors.toSet());
        this.beans.forEach(this::setFields);
    }

    private Object createBean(Class<?> targetClass) {
        try {
            Constructor<?> defaultConstructor = targetClass.getDeclaredConstructor();
            defaultConstructor.setAccessible(true);
            return defaultConstructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("기본 생성자가 존재하지 않습니다.");
        }
    }

    private void setFields(Object bean) {
        Class<?> beanClass = bean.getClass();
        for (Field declaredField : beanClass.getDeclaredFields()) {
            declaredField.setAccessible(true);
            injectDependency(bean, declaredField);
        }
    }

    private void injectDependency(Object bean, Field dependencyField) {
        try {
            Class<?> dependencyFieldType = dependencyField.getType();
            Object dependencyBean = getBean(dependencyFieldType);
            dependencyField.set(bean, dependencyBean);
        } catch (Exception ignored) {
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> targetBeanType) {
        return (T) beans.stream()
                .filter(bean -> targetBeanType.isAssignableFrom(bean.getClass()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 빈입니다."));
    }
}
