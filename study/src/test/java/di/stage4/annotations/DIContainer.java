package di.stage4.annotations;

import di.ConsumerWrapper;
import di.FunctionWrapper;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        beans = generateBeans(classes);
        beans.forEach(this::setFields);
    }

    private Set<Object> generateBeans(Set<Class<?>> classes) {
        return classes.stream()
                .map(FunctionWrapper.apply(Class::getDeclaredConstructor))
                .peek(constructor -> constructor.setAccessible(true))
                .map(FunctionWrapper.apply(Constructor::newInstance))
                .collect(Collectors.toUnmodifiableSet());
    }

    private void setFields(Object bean) {
        Arrays.stream(bean.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .forEach(field -> setField(bean, field));
    }

    private void setField(Object bean, Field field) {
        field.setAccessible(true);
        Class<?> fieldType = field.getType();
        beans.stream()
                .filter(fieldType::isInstance)
                .forEach(ConsumerWrapper.accept(matchBean -> field.set(bean, matchBean)));
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        return ClassPathScanner.getAllClassesInPackage(rootPackageName)
                .stream()
                .filter(DIContainer::hasTargetAnnotation)
                .collect(Collectors.collectingAndThen(Collectors.toSet(), DIContainer::new));
    }

    private static boolean hasTargetAnnotation(Class<?> clazz) {
        return clazz.isAnnotationPresent(Service.class) || clazz.isAnnotationPresent(Repository.class);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(aClass::isInstance)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("등록된 bean이 없습니다."));
    }
}
