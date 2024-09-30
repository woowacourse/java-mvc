package di.stage3.context;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DIContainer {

    private static final Logger log = LoggerFactory.getLogger(DIContainer.class);

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) throws Exception {
        this.beans = new LinkedHashSet<>();
        initializeBeans(classes);
    }

    private void initializeBeans(Set<Class<?>> classes) throws Exception {
        for (Class<?> clazz : classes) {
            Constructor<?> constructor = Arrays.stream(clazz.getDeclaredConstructors())
                    .filter(this::isConstructorEligible)
                    .findFirst()
                    .orElseThrow(IllegalStateException::new);

            constructor.setAccessible(true);
            Object[] parameters = resolveConstructorParameters(constructor.getParameterTypes());
            beans.add(constructor.newInstance(parameters));
        }
    }

    private boolean isConstructorEligible(Constructor<?> constructor) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        return countMatchedParameters(parameterTypes) == parameterTypes.length;
    }

    private int countMatchedParameters(Class<?>[] parameterTypes) {
        return (int) Arrays.stream(parameterTypes)
                .filter(parameterType -> getBean(parameterType) != null)
                .count();
    }

    private Object[] resolveConstructorParameters(Class<?>[] parameterTypes) {
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
