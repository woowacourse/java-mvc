package di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
                .map(clazz -> {
                    try {
                        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
                        Constructor<?> constructor = Arrays.stream(constructors)
                                .filter(constructor1 -> constructor1.getParameterTypes().length > 0)
                                .findFirst()
                                .orElse(clazz.getDeclaredConstructor());
                        constructor.setAccessible(true);
                        Object[] parameters = parameters(constructor, classes);
                        return constructor.newInstance(parameters);
                    } catch (InstantiationException |
                             IllegalAccessException |
                             InvocationTargetException |
                             NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toUnmodifiableSet());
    }

    private Object[] parameters(Constructor<?> constructor, Set<Class<?>> classes) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        return classes.stream()
                .filter(aClass -> {
                    for (Class<?> parameterType : parameterTypes) {
                        if (parameterType.isAssignableFrom(aClass)) {
                            return true;
                        }
                    }
                    return false;
                })
                .map(aClass -> {
                    try {
                        Constructor<?> declaredConstructor = aClass.getDeclaredConstructor();
                        declaredConstructor.setAccessible(true);
                        return declaredConstructor.newInstance();
                    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                             InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }).toArray();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(bean -> bean.getClass() == aClass)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("빈 없음"));
    }
}
