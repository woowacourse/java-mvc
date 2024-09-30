package di.stage3.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class DIContainer {

    private static final Logger log = LoggerFactory.getLogger(DIContainer.class);

    private Set<Object> beans = new HashSet<>();

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        this.beans.forEach(this::setFields);
    }

    private Set<Object> createBeans(final Set<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            try {
                Constructor<?>[] constructors = clazz.getDeclaredConstructors();
                for (Constructor<?> constructor : constructors) {
                    Class<?>[] parameterTypes = constructor.getParameterTypes();
                    if (parameterTypes.length == 0) {
                        Object instance = constructor.newInstance();
                        beans.add(instance);
                        break;
                    }
                    Object[] parameters = new Object[parameterTypes.length];
                    for (int i = 0; i < parameterTypes.length; i++) {
                        parameters[i] = getBean(parameterTypes[i]);
                    }
                    Object instance = constructor.newInstance(parameters);
                    beans.add(instance);
                }
            } catch (Exception e) {
                log.info("Failed to create bean for class: " + clazz.getName());
            }
        }
        return beans;
    }

    private void setFields(final Object bean) {
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().isAssignableFrom(UserDao.class)) {
                field.setAccessible(true); // 필드 접근 허용
                try {
                    Object fieldBean = getBean(field.getType());
                    if (fieldBean != null) {
                        field.set(bean, fieldBean);
                    } else {
                        log.info("No bean found for field: " + field.getName());
                    }
                } catch (IllegalAccessException e) {
                    log.info("Failed to set field: " + field.getName());
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
        log.info("No bean found for class: " + aClass.getName());
        return null;
    }
}
