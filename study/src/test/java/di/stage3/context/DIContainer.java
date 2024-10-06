package di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nullable;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = new HashSet<>();
        initializeBeans(classes);
    }

    private void initializeBeans(Set<Class<?>> beanClasses) {
        for (Class<?> beanClass : beanClasses) {
            beans.add(createBean(beanClass));
        }
        for (Object bean : beans) {
            injectBeanAtField(bean);
        }
    }

    private Object createBean(Class<?> beanClass) {
        try {
            Constructor<?> constructor = beanClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("해당 빈을 만드는 데 실패했습니다." + beanClass.getName(), e);
        }
    }

    private void injectBeanAtField(Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object fieldBean = getBean(field.getType());
            if (Modifier.isStatic(field.getModifiers()) || fieldBean == null) {
                continue;
            }
            try {
                field.setAccessible(true);
                field.set(bean, fieldBean);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("필드 주입에 실패했습니다: " + field.getName(), e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> T getBean(final Class<T> aClass) {
        for (Object bean : beans) {
            if (aClass.isInstance(bean)) {
                return (T) bean;
            }
        }
        return null;
    }
}
