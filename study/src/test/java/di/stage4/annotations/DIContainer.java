package di.stage4.annotations;

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

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        this.beans.forEach(this::setFields);
    }

    // 기본 생성자로 빈을 생성한다.
    private Set<Object> createBeans(final Set<Class<?>> classes) {

        return classes.stream()
                .map((final Class<?> aClass) -> {
                    try {
                        return aClass.getDeclaredConstructor();
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                })
                .peek(constructor -> constructor.setAccessible(true))
                .map(constructor1 -> {
                    try {
                        final Object instance = constructor1.newInstance();
                        constructor1.setAccessible(false);
                        return instance;
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toSet());
    }

    private void setFields(final Object bean) {
        final Field[] fields = bean.getClass()
                .getDeclaredFields();
        Arrays.stream(fields)
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .forEach(field -> {
                    field.setAccessible(true);
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

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        final Set<Class<?>> objects = ClassPathScanner.getAllClassesInPackage(rootPackageName
                , Repository.class, Service.class);
        return new DIContainer(objects);
    }
}
