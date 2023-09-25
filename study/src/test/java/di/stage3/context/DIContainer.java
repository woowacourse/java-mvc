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
        this.beans = setBeans(classes);
        this.beans.forEach(this::setFields);
    }

    private Set<Object> setBeans(Set<Class<?>> classes) throws Exception {
        Set<Object> beans = new HashSet<>();
        for (Class<?> aClass : classes) {
            Constructor<?> constructor = aClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object instance = constructor.newInstance();
            beans.add(instance);
        }
        return beans;
    }

    private void setFields(Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            beans.stream()
                    .filter(field.getType()::isInstance)
                    .findFirst()
                    .ifPresent(fieldInstance -> {
                        field.setAccessible(true);
                        try {
                            field.set(bean, fieldInstance);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(bean -> bean.getClass().equals(aClass))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("존재하지 않는 클래스입니다."));
    }
}
