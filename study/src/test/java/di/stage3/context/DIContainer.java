package di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = initializeBeans(classes);
        injectDependency(beans);
    }

    private Set<Object> initializeBeans(final Set<Class<?>> classes) {
        return classes.stream()
                .map(this::createInstance)
                .collect(Collectors.toSet());
    }

    private Object createInstance(final Class<?> aClass) {
        try {
            Constructor<?> declaredConstructor = aClass.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            return declaredConstructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create instance: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return beans.stream()
                .filter(bean -> aClass.isAssignableFrom(bean.getClass()))
                .map(bean -> (T) bean)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not exist bean of: " + aClass.getName()));
    }

    private void injectDependency(Set<Object> beans) {
        beans.forEach(bean -> {
            try {
                Field[] declaredFields = bean.getClass().getDeclaredFields();
                inject(bean, declaredFields);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to inject dependency: " + e.getMessage());
            }
        });
    }

    private void inject(Object bean, Field[] declaredFields) throws IllegalAccessException {
        for (var field : declaredFields) {
            injectDependencyIntoField(bean, field);
        }
    }

    private void injectDependencyIntoField(Object bean, Field field) throws IllegalAccessException {
        Object matched = beans.stream()
                .filter(b -> field.getType().isAssignableFrom(b.getClass()))
                .findFirst()
                .orElse(null);

        if (matched != null) {
            field.setAccessible(true);
            field.set(bean, matched);
        }
    }
}
