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

    private Set<Object> createBeans(Set<Class<?>> classes) {
        return classes.stream()
                .map(clazz -> {
                    try {
                        Constructor<?> constructor = clazz.getDeclaredConstructor();
                        constructor.setAccessible(true);
                        return constructor.newInstance();
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Bean Constructor Exception !!");
                    }
                })
                .collect(Collectors.toSet());
    }

    private void setFields(Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            Class<?> type = field.getType();
            field.setAccessible(true);
            try {
                Object injectField = beans.stream()
                        .filter(type::isInstance)
                        .findAny()
                        .orElse(null);
                if (injectField != null) {
                    field.set(bean, injectField);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Field Initialize Exception !!");
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(aClass::isInstance)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("no such bean"));
    }
}
