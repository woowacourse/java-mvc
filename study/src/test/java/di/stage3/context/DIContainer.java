package di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = new HashSet<>();
        classes.forEach(c -> {
            try {
                Constructor<?> defaultConstructor = c.getDeclaredConstructor();
                defaultConstructor.setAccessible(true);
                beans.add(defaultConstructor.newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        injectFieldDependency();
    }

    private void injectFieldDependency() {
        beans.forEach(bean -> {
            Field[] fields = bean.getClass().getDeclaredFields();
            Arrays.stream(fields)
                    .forEach(field -> setField(field, bean));
        });
    }

    private void setField(Field field, Object bean) {
        try {
            field.setAccessible(true);
            for (Object o : beans) {
                if (field.getType().isAssignableFrom(o.getClass())) {
                    field.set(bean, o);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(aClass::isInstance)
                .findAny()
                .orElse(null);
    }
}
