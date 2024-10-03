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
        this.beans = createBeans(classes);
        this.beans.forEach(this::setFields);
    }

    // 기본 생성자로 빈을 생성한다.
    private Set<Object> createBeans(final Set<Class<?>> classes) {
        return classes.stream()
                .map(this::createInstanceFromDefaultConstructor)
                .collect(Collectors.toUnmodifiableSet());
    }

    private Object createInstanceFromDefaultConstructor(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("기본 생성자로 빈을 생성하는데 실패했습니다.");
        }
    }

    private void setFields(final Object bean) {
        for (Field field : bean.getClass().getDeclaredFields()) {
            setField(bean, field);
        }
    }

    private void setField(Object bean, Field field) {
        Class<?> fieldType = field.getType();
        beans.stream()
                .filter(fieldType::isInstance)
                .forEach(matchBean -> injectField(bean, field, matchBean));
    }


    private void injectField(Object bean, Field field, Object matchBean) {
        try {
            field.setAccessible(true);
            field.set(bean, matchBean);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T getBean(final Class<T> aClass) {
        return beans.stream()
                .filter(aClass::isInstance)
                .map(bean -> (T) bean)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("bean을 찾을 수 없습니다."));
    }
}
