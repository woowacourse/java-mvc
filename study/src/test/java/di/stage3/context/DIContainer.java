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
        this.beans.forEach(this::initFields);
    }

    private Set<Object> createBeans(final Set<Class<?>> classes) {
        final Set<Object> beans = new HashSet<>();
        for (Class<?> clazz : classes) {
            beans.add(instantiateClass(clazz));
        }
        return beans;
    }

    private Object instantiateClass(final Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                 InvocationTargetException e) {
            throw new IllegalArgumentException("인스턴스화 할 수 없습니다.");
        }
    }

    private void initFields(final Object bean) {
        final Field[] fields = bean.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                initField(bean, field);
            }
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("초기화할 수 없는 필드입니다.");
        }
    }

    private void initField(Object bean, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        final Class<?> fieldType = field.getType();
        for (Object o : beans) {
            if (fieldType.isAssignableFrom(o.getClass())) {
                field.set(bean, o);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(bean -> aClass.isAssignableFrom(bean.getClass()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 빈입니다."));
    }
}
