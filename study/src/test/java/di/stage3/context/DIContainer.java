package di.stage3.context;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Map<Class<?>, Object> beans = new HashMap<>();

    public DIContainer(final Set<Class<?>> classes) {
        classes.stream()
                .sorted(Comparator.comparing(this::canInstantiateAlone).reversed())
                .forEach(this::registerBean);
    }

    private boolean canInstantiateAlone(final Class<?> clazz) {
        if (clazz.isInterface()) {
            return false;
        }

        final Constructor<?> constructor = getConstructor(clazz);
        return Arrays.stream(constructor.getParameterTypes())
                .allMatch(this::canInstantiateAlone);
    }

    private void registerBean(final Class<?> clazz) {
        if (beans.containsKey(clazz)) {
            return;
        }

        final Constructor<?> constructor = getConstructor(clazz);
        final List<Object> parameters = getParameters(constructor);
        final Object instance = createInstance(parameters, constructor);

        final Class<?>[] interfaceTypes = clazz.getInterfaces();
        for (final Class<?> interfaceType : interfaceTypes) {
            beans.put(interfaceType, instance);
        }
        beans.put(clazz, instance);
    }

    private Constructor<?> getConstructor(final Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructors()[0];
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Object> getParameters(final Constructor<?> constructor) {
        final Class<?>[] parameterTypes = constructor.getParameterTypes();

        final List<Object> parameters = new ArrayList<>();
        for (final Class<?> parameterType : parameterTypes) {
            if (!beans.containsKey(parameterType)) {
                registerBean(parameterType);
            }

            Object parameter = getBean(parameterType);
            parameters.add(parameter);
        }

        return parameters;
    }

    private Object createInstance(final List<Object> parameters, final Constructor<?> constructor) {
        try {
            if (parameters.isEmpty()) {
                return constructor.newInstance();
            }

            return constructor.newInstance(parameters.toArray());
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.getOrDefault(aClass, null);
    }
}
