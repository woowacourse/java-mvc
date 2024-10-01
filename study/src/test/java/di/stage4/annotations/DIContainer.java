package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.beans = new HashSet<>();

        for (Class<?> clazz : classes) {
            Constructor<?> declaredConstructor = clazz.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            beans.add(declaredConstructor.newInstance());
        }

        beans.forEach(this::injectDependencies);
    }

    public static DIContainer createContainerForPackage(final String rootPackageName)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Set<Class<?>> allClassesInPackage = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(allClassesInPackage);
    }

    private void injectDependencies(Object instance) {
        Stream.of(instance.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .forEach(field -> {
                    field.setAccessible(true);
                    setBeanIntoInstance(instance, field);
                });
    }

    private void setBeanIntoInstance(Object instance, Field field) {
        beans.stream()
                .filter(bean -> field.getType().isAssignableFrom(bean.getClass()))
                .findFirst()
                .ifPresent(bean -> setBeanIntoField(instance, field, bean));
    }

    private void setBeanIntoField(Object instance, Field field, Object bean) {
        try {
            field.set(instance, bean);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to inject dependency for field: " + field.getName(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(aClass::isInstance)
                .findAny()
                .orElse(null);
    }
}
