package di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        this.beans = new HashSet<>();
        createBeans(classes);
        for (Object bean : beans) {
            setFields(bean);
        }
    }

    private void createBeans(Set<Class<?>> classes)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (Class<?> aClass : classes) {
            Constructor<?> constructor = aClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object bean = constructor.newInstance();
            beans.add(bean);
        }
    }

    private void setFields(Object bean) throws IllegalAccessException {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            Class<?> filedType = field.getType();
            Optional<Object> beanField = beans.stream()
                    .filter(filedType::isInstance)
                    .findFirst();
            if (beanField.isPresent()) {
                field.set(bean, beanField.get());
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(object -> object.getClass().equals(aClass))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }
}
