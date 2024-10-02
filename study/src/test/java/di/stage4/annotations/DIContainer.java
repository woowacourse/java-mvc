package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
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
        classes.forEach(clazz -> registerBean(clazz, classes));
    }

    public void registerBean(Class<?> clazz, Set<Class<?>> classes) {
        if (getBean(clazz) != null) {
            return;
        }
        classes.stream().filter(clazz::isAssignableFrom)
                .findFirst()
                .ifPresentOrElse(
                        actual -> beans.put(actual, createInstance(actual, classes)),
                        () -> {
                            throw new IllegalArgumentException("클래스에 해당하는 빈이 없습니다.");
                        }
                );
    }

    private Object createInstance(Class<?> clazz, Set<Class<?>> classes) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object instance = constructor.newInstance();

            Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Inject.class))
                    .forEach(field -> setField(classes, field, instance));

            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setField(Set<Class<?>> classes, Field field, Object instance) {
        try {
            Class<?> type = field.getType();
            Object factor = getOrRegister(type, classes);
            field.setAccessible(true);
            field.set(instance, factor);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Object getOrRegister(Class<?> clazz, Set<Class<?>> classes) {
        Object bean = getBean(clazz);
        if (bean == null) {
            registerBean(clazz, classes);
            bean = getBean(clazz);
        }
        return bean;
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        return new DIContainer(ClassPathScanner.getAllClassesInPackage(rootPackageName));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        if (beans.containsKey(aClass)) {
            return (T) beans.get(aClass);
        }

        Class<?> clazz = beans.keySet().stream()
                .filter(aClass::isAssignableFrom)
                .findFirst()
                .orElse(null);

        if (clazz == null) {
            return null;
        }
        return (T) beans.get(clazz);
    }
}
