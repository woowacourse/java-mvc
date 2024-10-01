package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private static final Logger log = LoggerFactory.getLogger(DIContainer.class);

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = new HashSet<>();
        initInstance(classes);
        injectRelations(classes);
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> allClassesInPackage = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(allClassesInPackage);
    }

    private void initInstance(Set<Class<?>> classes) {
        for (Class<?> aClass : classes) {
            Object instance = getInstance(aClass);
            beans.add(instance);
        }
    }

    private Object getInstance(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InstantiationException |
                 IllegalAccessException |
                 NoSuchMethodException |
                 InvocationTargetException e) {
            log.error("Failed to instantiate " + clazz.getName(), e);
            return null;
        }
    }

    private void injectRelations(Set<Class<?>> classes) {
        for (Class<?> aClass : classes) {
            initFields(aClass);
        }
    }

    private void initFields(Class<?> clazz) {
        Object curBean = getBean(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            Optional<? extends Class<?>> optionalBeanClass = getAssignableBean(field);
            if (optionalBeanClass.isPresent()) {
                Object assignableBean = getBean(optionalBeanClass.get());
                setField(field, curBean, assignableBean);
            }
        }
    }

    private Optional<? extends Class<?>> getAssignableBean(Field field) {
        Class<?> fieldType = field.getType();
        return beans.stream()
                .map(Object::getClass)
                .filter(fieldType::isAssignableFrom)
                .findFirst();
    }

    private static void setField(Field field, Object initBean, Object assignable) {
        field.setAccessible(true);
        try {
            field.set(initBean, assignable);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return beans.stream()
                .filter(aClass::isInstance)
                .map(aClass::cast)
                .findFirst()
                .orElse(null);
    }
}
