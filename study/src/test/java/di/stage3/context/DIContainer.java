package di.stage3.context;

import di.ConsumerWrapper;
import di.FunctionWrapper;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = getBeans(classes);
    }

    private Set<Object> getBeans(Set<Class<?>> classes) {
        return classes.stream()
                .map(FunctionWrapper.apply(this::getConstructor))
                .peek(ConsumerWrapper.accept(constructor -> constructor.setAccessible(true)))
                .map(FunctionWrapper.apply(
                        constructor -> constructor.newInstance(getConstructorParameters(constructor, classes))))
                .collect(Collectors.toUnmodifiableSet());
    }

    private Constructor<?> getConstructor(Class<?> clazz) throws NoSuchMethodException {
        return Arrays.stream(clazz.getDeclaredConstructors())
                .filter(constructor -> constructor.getParameterTypes().length > 0)
                .findFirst()
                .orElse(clazz.getDeclaredConstructor());
    }

    private Object[] getConstructorParameters(Constructor<?> constructor, Set<Class<?>> classes) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        return classes.stream()
                .filter(aClass -> Arrays.stream(parameterTypes)
                        .anyMatch(parameterType -> parameterType.isAssignableFrom(aClass)))
                .map(FunctionWrapper.apply(Class::getDeclaredConstructor))
                .peek(parameterConstructor -> parameterConstructor.setAccessible(true))
                .map(FunctionWrapper.apply(Constructor::newInstance))
                .toArray();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(bean -> bean.getClass() == aClass)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("빈 없음"));
    }
}
