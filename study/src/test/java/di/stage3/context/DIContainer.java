package di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        injectDependencies();
    }

    private Set<Object> createBeans(final Set<Class<?>> classes) {
        return classes.stream()
                .map(this::createInstance)
                .collect(Collectors.toSet());
    }

    private Object createInstance(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("빈 생성 중 오류가 발생했습니다");
        }
    }

    private void injectDependencies() {
        beans.forEach(this::setFields);
    }

    private void setFields(final Object bean) {
        Arrays.stream(bean.getClass().getDeclaredFields())
                .forEach(field -> injectField(bean, field));
    }

    private void injectField(Object bean, Field field) {
        field.setAccessible(true);
        try {
            Object fieldBean = beans.stream()
                    .filter(aBean -> field.getType().isAssignableFrom(aBean.getClass()))
                    .findFirst()
                    .orElse(null);
            if (fieldBean != null) {
                field.set(bean, fieldBean);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("필드 주입 중 오류 발생");
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(b -> aClass.isAssignableFrom(b.getClass()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 타입의 빈을 찾을 수 없습니다"));
    }
}
