package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.ReflectiveOperationException;
import java.util.HashSet;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        final Set<Object> instances = new HashSet<>();
        for (final Class<?> clazz : classes) {
            instances.add(createBean(clazz));
        }
        this.beans = Set.copyOf(instances);
        beans.forEach(this::injectFields);
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        return new DIContainer(ClassPathScanner.getAllClassesInPackage(rootPackageName));
    }

    public <T> T getBean(final Class<T> aClass) {
        return beans.stream()
                .filter(aClass::isInstance)
                .map(aClass::cast)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No bean found: " + aClass.getName()));
    }

    private Object createBean(final Class<?> clazz) {
        try {
            final Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot create bean: " + clazz.getName(), e);
        }
    }

    private void injectFields(final Object bean) {
        for (final Field field : bean.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(Inject.class)) {
                continue;
            }
            field.setAccessible(true);
            try {
                field.set(bean, getBean(field.getType()));
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Cannot inject field: " + field.getName(), e);
            }
        }
    }
}
