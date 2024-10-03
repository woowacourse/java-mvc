package di.stage3.context;

import org.springframework.beans.factory.annotation.Autowired;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = new HashSet<>();

        // 1. 먼저 각 클래스를 인스턴스화하여 beans에 추가
        for (Class<?> clazz : classes) {
            try {
                Constructor<?> declaredConstructor = clazz.getDeclaredConstructor();
                declaredConstructor.setAccessible(true);
                Object instance = declaredConstructor.newInstance();
                beans.add(instance);
            } catch (Exception e) {
                throw new RuntimeException("Failed to instantiate class: " + clazz, e);
            }
        }

        for (Object bean : beans) {
            injectDependencies(bean);
        }
    }

    private void injectDependencies(Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        Arrays.stream(fields)
                .forEach(field -> injectField(bean, field));
    }

    private void injectField(Object bean, Field field) {
        beans.stream()
                .filter(obj -> field.getType().isAssignableFrom(obj.getClass()))
                .findFirst()
                .ifPresent(obj -> setField(field, bean, obj));
    }

    private void setField(Field field, Object bean, Object obj) {
        try {
            field.setAccessible(true);
            field.set(bean, obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        for (Object bean : beans) {
            if (aClass.isInstance(bean)) {
                return aClass.cast(bean);
                }
        }
        throw new RuntimeException("No bean found for class: " + aClass.getName());
    }
}
