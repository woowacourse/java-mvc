package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Map<Class<?>, Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = new HashMap<>();
        classes.forEach(this::add);
        classes.forEach(this::addDependency);
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> injectedClasses = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(injectedClasses);
    }

    private void add(Class<?> clazz) {
        if(alreadyAddedBean(clazz)) {
            return;
        }
        Object object = createBean(clazz);
        beans.put(clazz, object);
    }

    private boolean alreadyAddedBean(Class<?> clazz) {
        return beans.get(clazz) != null;
    }


    private Object createBean(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("There is no default constructor in class : " + clazz.getCanonicalName());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Cannot create new instance in default constructor in class : " + clazz.getCanonicalName());
        }
    }

    private void addDependency(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            Object dependingBean = findDependingBean(field);
            if(dependingBean == null) {
                return;
            }
            try {
                field.set(beans.get(clazz), dependingBean);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Cannot inject dependency in field: " + field.getName(), e);
            }
        }
    }

    private Object findDependingBean(Field field) {
        return beans.entrySet().stream()
            .filter(entry -> field.getType().isAssignableFrom(entry.getKey()))
            .map(Map.Entry::getValue)
            .findAny()
            .orElse(null);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> aClass) {
        return (T) beans.entrySet().stream()
            .filter(entry -> aClass.isAssignableFrom(entry.getKey()))
            .map(Map.Entry::getValue)
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("Does not exist bean: " + aClass.getCanonicalName()));
    }
}
