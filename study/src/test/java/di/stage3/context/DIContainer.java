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

    public DIContainer(final Set<Class<?>> classes) throws Exception {
        this.beans = new HashSet<>();
        this.beans.addAll(createBeans(classes));
        for (Object bean : beans) {
            setFields(bean);
        }
    }

    private Set<Object> createBeans(Set<Class<?>> classes) throws Exception {
        Set<Object> result = new HashSet<>();
        for (Class<?> aClass : classes) {
            Constructor<?> constructor = aClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object instance = constructor.newInstance();
            constructor.setAccessible(false);
            result.add(instance);
        }
        return result;
    }

    private void setFields(Object bean) throws Exception {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            setField(bean, field);
        }
    }

    private void setField(Object bean, Field field) throws Exception {
        field.setAccessible(true);
        if (field.get(bean) != null) {
            return;
        }
        Object fieldInstance = findFieldBean(field);
        field.set(bean, fieldInstance);
    }

    private Object findFieldBean(Field field) {
        return beans.stream()
                .filter(b -> canAssign(field, b))
                .findAny()
                .orElse(null);
    }

    private boolean canAssign(Field field, Object bean) {
        return bean.getClass().equals(field.getType()) || field.getType().isAssignableFrom(bean.getClass());
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(bean -> bean.getClass().equals(aClass))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 빈이 존재하지 않습니다: %s"
                        .formatted(aClass.getSimpleName())));
    }
}
