package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private static final Class<Service> SERVICE_ANNOTATION = Service.class;
    private static final Class<Repository> REPOSITORY_ANNOTATION = Repository.class;
    private static final Class<Inject> INJECT_ANNOTATION = Inject.class;

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        this.beans.forEach(this::setFields);
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> classes = ClassPathScanner.getAnnotatedClassesInPackage(
                rootPackageName,
                SERVICE_ANNOTATION,
                REPOSITORY_ANNOTATION);

        return new DIContainer(classes);
    }

    private Set<Object> createBeans(final Set<Class<?>> classes) {
        return classes.stream()
                .map(this::createInstance)
                .collect(Collectors.toSet());
    }

    private Object createInstance(final Class<?> aClass) {
        try {
            Constructor<?> declaredConstructor = aClass.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            return declaredConstructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void setFields(final Object bean) {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        Arrays.stream(declaredFields)
                .filter(this::isInjectable)
                .forEach(field -> injectField(field, bean));
    }

    private boolean isInjectable(final Field field) {
        int modifiers = field.getModifiers();
        return !Modifier.isStatic(modifiers) &&
                !Modifier.isFinal(modifiers) &&
                field.isAnnotationPresent(INJECT_ANNOTATION);
    }

    private void injectField(final Field field, final Object bean) {
        field.setAccessible(true);
        Object fieldInstance = getBean(field.getType());

        try {
            field.set(bean, fieldInstance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return beans.stream()
                .filter(bean -> aClass.isAssignableFrom(bean.getClass()))
                .findFirst()
                .map(aClass::cast)
                .orElseThrow(() -> new RuntimeException("Beand을 찾을 수 없습니다."));
    }
}
