package di.stage3.context;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) throws Exception {
        beans = new LinkedHashSet<>();

        for (Class<?> aClass : classes) {
            Constructor<?>[] constructors = aClass.getDeclaredConstructors();
            Object instance = findInstance(constructors);
            beans.add(instance);
        }
    }

    private Object findInstance(Constructor<?>[] constructors) throws Exception {
        for (Constructor<?> constructor : constructors) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();

            return findBean(constructor, parameterTypes);
        }
        return null;
    }

    private Object findBean(Constructor constructor, Class<?>[] parameterTypes) throws Exception {
        if (countMatchedParameter(parameterTypes) == parameterTypes.length) {
            constructor.setAccessible(true);
            Object[] beanOf = getBeanOf(parameterTypes);
            return constructor.newInstance(beanOf);
        }
        return null;
    }

    private int countMatchedParameter(Class<?>[] parameterTypes) {
        return (int) Arrays.stream(parameterTypes)
                .filter(parameter -> getBean(parameter) != null)
                .count();
    }

    private Object[] getBeanOf(Class<?>[] parameterTypes) {
        return Arrays.stream(parameterTypes)
                .map(this::getBean)
                .toArray();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(bean -> aClass.isAssignableFrom(bean.getClass()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
