package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;

import di.ConsumerWrapper;
import di.FunctionWrapper;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        this.beans.forEach(this::setFields);
    }

    private Set<Object> createBeans(final Set<Class<?>> classes) {
        return classes.stream()
                .map(FunctionWrapper.apply(Class::getDeclaredConstructor))
                .peek(constructor -> constructor.setAccessible(true))
                .map(FunctionWrapper.apply(Constructor::newInstance))
                .collect(Collectors.toUnmodifiableSet());
    }

    private void setFields(final Object bean) {
        for (final var field : bean.getClass().getDeclaredFields()) {
            setFields(bean, field);
        }
    }

    private void setFields(final Object bean, final Field field) {
        final var fieldType = field.getType();
        field.setAccessible(true);

        beans.stream()
                .filter(fieldType::isInstance)
                .forEach(ConsumerWrapper.accept(matchBean -> field.set(bean, matchBean)));
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        return new DIContainer(ClassPathScanner.getAllClassesInPackage(rootPackageName));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(aClass::isInstance)
                .findAny()
                .orElseThrow(() -> new UnsupportedOperationException("해당 빈이 존재하지 않습니다."));
    }
}
