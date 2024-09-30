package di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        this.beans.forEach(this::injectFields);
    }

    private Set<Object> createBeans(Set<Class<?>> clazzes) {
        Set<Object> beans = new HashSet<>();
        for (Class<?> clazz : clazzes) {
            beans.add(createBean(clazz));
        }
        return beans;
    }

    private Object createBean(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true); // private 생성자일 경우 접근 가능하도록 설정
            return constructor.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalStateException("Cannot instantiate bean of type: " + clazz.getName(), e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot access constructor of bean type: " + clazz.getName(), e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException("Constructor of bean type " + clazz.getName() + " threw an exception: " + e.getCause(), e);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("No default constructor for bean type: " + clazz.getName(), e);
        }
    }

    private void injectFields(Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                injectField(bean, field);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Cannot access field: " + field.getName(), e);
            }
        }
    }

    private void injectField(Object targetBean, Field field) throws IllegalAccessException {
        field.setAccessible(true); // private 필드일 경우 접근 가능하도록 설정
        Class<?> fieldType = field.getType();
        for (Object bean : beans) {
            if (fieldType.isAssignableFrom(bean.getClass())) {
                field.set(targetBean, bean);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> clazz) {
        for (Object bean : beans) {
            if (clazz.isAssignableFrom(bean.getClass())) {
                return (T) bean;
            }
        }
        return null;
    }
}
