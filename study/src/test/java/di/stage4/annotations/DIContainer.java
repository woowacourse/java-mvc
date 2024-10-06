package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        beans.forEach(this::setFields);
    }

    private Set<Object> createBeans(Set<Class<?>> classes) {
        return classes.stream()
                .map(this::createInstance)
                .collect(Collectors.toSet());
    }

    private Object createInstance(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException("인스턴스 생성 실패 : " + e.getMessage());
        }
    }

    private void setFields(Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        Arrays.stream(fields).forEach(field -> {
            try {
                injectFieldInstance(bean, field);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("의존성 주입 실패 : " + e.getMessage());
            }
        });
    }

    private void injectFieldInstance(Object bean, Field field) throws IllegalAccessException {
        Object fieldInstance = beans.stream()
                .filter(instance -> field.getType().isAssignableFrom(instance.getClass()))
                .findFirst()
                .orElse(null);
        if (fieldInstance != null) {
            field.setAccessible(true);
            field.set(bean, fieldInstance);
        }
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> classes = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(classes);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(bean -> aClass.isAssignableFrom(bean.getClass()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("%s Bean은 존재하지 않습니다."
                        .formatted(aClass.getName())));
    }
}
