package di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
        Set<Object> beans = new HashSet<>();
        for (Class<?> aClass : classes) {
            try {
                Constructor<?> constructor = aClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                beans.add(constructor.newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return beans;
    }

    // 빈 내부에 선언된 필드를 각각 셋팅한다.
    // 각 필드에 빈을 대입(assign)한다.
    private void setFields(final Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        Arrays.stream(fields)
                .forEach(field -> {
                    try {
                        field.setAccessible(true);
                        if (Modifier.isStatic(field.getModifiers()) || Objects.nonNull(field.get(bean))) {
                            return;
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        field.set(bean, getBean(field.getType()));
                    } catch (final IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    // 빈 컨텍스트(DI)에서 관리하는 빈을 찾아서 반환한다.
    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return beans.stream()
                .filter(aClass::isInstance)
                .map(aClass::cast)
                .findFirst()
                .orElse(null);
    }
}
