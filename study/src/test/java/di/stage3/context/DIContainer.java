package di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        final Set<Object> beans = new HashSet<>();
        for (Class<?> object : classes) {
            try {
                final Constructor declaredConstructor = object.getDeclaredConstructor();
                declaredConstructor.setAccessible(true);
                beans.add(declaredConstructor.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        createConstructor(beans);
        this.beans = beans;
    }

    private void createConstructor(final Set<Object> beans) {
        for (Object bean : beans) {
            Field[] declaredFields = bean.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                setConstructor(beans, bean, declaredField);
            }
        }
    }

    private void setConstructor(final Set<Object> beans, final Object bean, final Field declaredField) {
        for (Object obj : beans) {
            if (declaredField.getType().isAssignableFrom(obj.getClass())) {
                try {
                    declaredField.setAccessible(true);
                    declaredField.set(bean, obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        for (Object bean : beans) {
            if (aClass.isAssignableFrom(bean.getClass())) {
                return (T) bean;
            }
        }
        return null;
    }
}
