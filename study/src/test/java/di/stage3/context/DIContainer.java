package di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DIContainer {

    private static final Logger log = LoggerFactory.getLogger(DIContainer.class);

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) throws ReflectiveOperationException {
        this.beans = createBeans(classes);
        this.beans.forEach(this::setFields);
    }

    private Set<Object> createBeans(Set<Class<?>> classes) throws ReflectiveOperationException {
        Set<Object> createdBeans = new HashSet<>();
        for (Class<?> clazz : classes) {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object instance = constructor.newInstance();
            createdBeans.add(instance);
        }
        return createdBeans;
    }

    private void setFields(Object bean) {
        for (Field field : bean.getClass().getDeclaredFields()) {
            Optional<Object> dependency = findBean(field.getType());
            dependency.ifPresent(value -> {
                try {
                    field.setAccessible(true);
                    field.set(bean, value);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            });
        }
    }

    private Optional<Object> findBean(Class<?> type) {
        return beans.stream()
                .filter(type::isInstance)
                .findFirst();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        Optional<Object> bean = findBean(aClass);
        return (T) bean.orElseThrow(IllegalStateException::new);
    }
}
