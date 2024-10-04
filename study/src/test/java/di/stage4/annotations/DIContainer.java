package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Equivalent to Spring's BeanFactory or ApplicationContext.
 */
class DIContainer {

    private final Set<Object> beans;

    public static DIContainer createContainerForPackage(final String rootPackageName) throws Exception {
        Set<Class<?>> allClassesInPackage = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(allClassesInPackage);
    }

    public DIContainer(final Set<Class<?>> classes) throws Exception {
        this.beans = new LinkedHashSet<>();
        initializeBeans(classes);
        injectFields();
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

    private void injectFields() throws IllegalAccessException {
        for (Object bean : beans) {
            for (Field field : bean.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Inject.class)) {
                    field.setAccessible(true);
                    field.set(bean, getBean(field.getType()));
                }
            }
        }
    }
}
