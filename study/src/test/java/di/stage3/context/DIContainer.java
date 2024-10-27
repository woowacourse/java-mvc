package di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        beans = classes.stream()
                .map(this::createBean)
                .collect(Collectors.toSet());
        beans.forEach(this::injectDependency);
        beans.forEach(this::validateDependency);
    }

    private Object createBean(Class<?> aClass) {
        try {
            Constructor<?> defaultConstructor = aClass.getDeclaredConstructor();
            defaultConstructor.setAccessible(true);
            return defaultConstructor
                    .newInstance();
        } catch (Exception e) {
            throw new RuntimeException("인스턴스를 생성할 수 없습니다.", e);
        }
    }

    private void injectDependency(Object bean) {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            if (isFieldInjected(bean, field)) {
                continue;
            }
            injectField(bean, field);
        }

    }

    private boolean isFieldNull(Object bean, Field field) {
        try {
            return field.get(bean) == null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException("필드를 조회할 수 없습니다.");
        }
    }

    private boolean isFieldInjected(Object bean, Field field) {
        return !isFieldNull(bean, field);
    }

    private void injectField(Object bean, Field field) {
        try {
            field.set(bean, getBean(field.getType()));
        } catch (IllegalAccessException e) {
            throw new RuntimeException("필드를 주입할 수 없습니다.");
        }
    }

    private void validateDependency(Object bean) {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            if (isFieldNull(bean, field)) {
                throw new RuntimeException(
                        bean.getClass().getName() + ": " + field.getName() + " 의존성이 주입되지 않았습니다.");
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(bean -> aClass.isAssignableFrom(bean.getClass()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(aClass + " 에 해당하는 빈이 존재하지 않습니다."));
    }
}
