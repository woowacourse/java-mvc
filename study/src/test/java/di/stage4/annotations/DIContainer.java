package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
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

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> classes = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(classes);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(bean -> aClass.isAssignableFrom(bean.getClass()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 빈이 존재하지 않습니다."));
    }

    private Set<Object> createBeans(final Set<Class<?>> classes) {
        Set<Object> beans = new HashSet<>();
        classes.stream()
                .filter(this::isServiceOrRepository)
                .forEach(aClass -> createBean(aClass, beans));

        return beans;
    }

    private boolean isServiceOrRepository(Class<?> aClass) {
        return aClass.isAnnotationPresent(Service.class) || aClass.isAnnotationPresent(Repository.class);
    }

    private void createBean(Class<?> aClass, Set<Object> beans) {
        try {
            Constructor<?> constructor = aClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object instance = constructor.newInstance();
            beans.add(instance);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException("빈 생성에 실패했습니다.");
        }
    }

    private void setFields(final Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();

        Arrays.stream(fields)
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .filter(field -> !Modifier.isFinal(field.getModifiers()))
                .forEach(field -> setField(bean, field));
    }

    private void setField(Object bean, Field field) {
        Class<?> type = field.getType();

        Object inject = beans.stream()
                .filter(type::isInstance)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("주입할 빈이 존재하지 않습니다."));

        try {
            field.setAccessible(true);
            field.set(bean, inject);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("필드 주입에 실패했습니다.");
        }
    }
}
