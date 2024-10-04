package di.stage4.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans = new HashSet<>();
    private static final List<Class<? extends Annotation>> SCAN_ANNOTATIONS = List.of(Repository.class, Service.class);
    private static final Class<? extends Annotation> INJECTION_ANNOTATION = Inject.class;

    public DIContainer(final Set<Class<?>> classes) {
        classes.forEach(this::registerBean);
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Reflections reflections = new Reflections(rootPackageName);
        Set<Class<?>> annotatedClasses = SCAN_ANNOTATIONS.stream()
                .flatMap(annotation -> reflections.getTypesAnnotatedWith(annotation).stream())
                .collect(Collectors.toSet());
        return new DIContainer(annotatedClasses);
    }

    private void registerBean(Class<?> clazz) {
        clazz = getImplementor(clazz);
        if (isRegistered(clazz)) {
            return;
        }

        Field[] declaredFields = clazz.getDeclaredFields();
        Arrays.stream(declaredFields)
                .filter(field -> field.isAnnotationPresent(INJECTION_ANNOTATION))
                .forEach(field -> registerBean(field.getType()));

        beans.add(createInstanceWithInjection(clazz));
    }

    private Class<?> getImplementor(Class<?> clazz) {
        if (clazz.isInterface()) {
            Reflections reflection = new Reflections(clazz.getPackageName());
            return reflection.getSubTypesOf(clazz).stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("구현 클래스가 존재하지 않습니다."));
        }
        return clazz;
    }

    private boolean isRegistered(Class<?> clazz) {
        return beans.stream()
                .anyMatch(clazz::isInstance);
    }

    private Object createInstanceWithInjection(Class<?> clazz) {
        Object instance = getInstance(clazz);
        Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(INJECTION_ANNOTATION))
                .forEach(field -> injectFields(instance, field));
        return instance;
    }

    private Object getInstance(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException
                 | InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(clazz.getName() + "의 기본 생성자가 존재하지 않습니다.");
        }
    }

    private void injectFields(Object instance, Field field) {
        Class<?> fieldType = field.getType();
        Object fieldInstance = getBean(fieldType);
        field.setAccessible(true);

        try {
            field.set(instance, fieldInstance);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("필드 주입에 실패했습니다.");
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) this.beans.stream()
                .filter(aClass::isInstance)
                .findAny()
                .orElseThrow(() -> new IllegalStateException(aClass.getName() + "은 존재하지 않는 빈입니다."));
    }
}
