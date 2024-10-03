package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import di.ConsumerWrapper;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = classes.stream()
                .map(this::getDeclaredConstructor)
                .map(this::initInstance)
                .collect(Collectors.toUnmodifiableSet());
        this.beans.forEach(this::setFields);
    }

    private Object initInstance(final Constructor<?> constructor) {
        try {
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (final InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("인스턴스를 생성할 수 없습니다.", e);
        }
    }

    private Constructor<?> getDeclaredConstructor(final Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor();
        } catch (final NoSuchMethodException e) {
            throw new IllegalStateException("해당하는 생성자가 없습니다.");
        }
    }

    private void setFields(final Object bean) {
        Arrays.stream(bean.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .forEach(field -> setFields(bean, field));
    }

    private void setFields(final Object bean, final Field field) {
        final Class<?> fieldType = field.getType();
        field.setAccessible(true);

        beans.stream()
                .filter(fieldType::isInstance)
                .forEach(ConsumerWrapper.accept(matchBean -> field.set(bean, matchBean)));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return beans.stream()
                .filter(aClass::isInstance)
                .map(aClass::cast)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 빈이 없습니다."));
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        final Set<Class<?>> allClassesInPackage = ClassPathScanner.getAllClassesInPackage(rootPackageName)
                .stream()
                .filter(DIContainer::isComponent)
                .collect(Collectors.toUnmodifiableSet());
        return new DIContainer(allClassesInPackage);
    }

    private static boolean isComponent(final Class<?> aClass) {
        return aClass.isAnnotationPresent(Service.class) || aClass.isAnnotationPresent(Repository.class);
    }
}
