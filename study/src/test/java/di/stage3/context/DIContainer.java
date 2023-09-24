package di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Class<?>> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = classes;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) throws Exception {
        final Class<?> clazz = findBean(aClass);
        final Object instance = createInstance(clazz);
        final Field[] fields = clazz.getDeclaredFields();
        for (final Field field : fields) {
            final Class<?> parameterClazz = findBean(field.getType());
            final Object fieldInstance = createInstance(parameterClazz);
            field.setAccessible(true);
            field.set(instance, fieldInstance);
        }
        return (T) instance;
    }

    private Object createInstance(final Class<?> clazz) throws Exception{
        final Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    private <T> Class<?> findBean(final Class<T> aClass) {
        return beans.stream()
                .filter(aClass::isAssignableFrom)
                .findFirst()
                .orElseThrow();
    }
}
