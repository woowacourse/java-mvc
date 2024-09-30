package di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private static final Logger log = LoggerFactory.getLogger(DIContainer.class);
    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.beans = new HashSet<>();
        for (Class<?> aClass : classes) {
            Constructor<?> declaredConstructor = aClass.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            Object newInstance = declaredConstructor.newInstance();
            beans.add(newInstance);
            injectDependencies(newInstance);
        }
    }

    private void injectDependencies(Object instance) {
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field : fields) {
            for (Object bean : beans) {
                if (field.getType().isInstance(bean)) {
                    field.setAccessible(true);
                    try {
                        field.set(instance, bean);
                    } catch (IllegalAccessException e) {
                        log.error("Failed to inject dependency for field: " + field.getName(), e);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        for (Object bean : beans) {
            if (aClass.isInstance(bean)) {
                return (T) bean;
            }
        }
        return null;
    }
}
