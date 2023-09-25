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
        this.beans = createBeans(classes);
        this.beans.forEach(this::setFields);
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> allClassesInPackage = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return allClassesInPackage.stream()
                .filter(DIContainer::isAnnotationPresent)
                .collect(Collectors.collectingAndThen(Collectors.toUnmodifiableSet(), DIContainer::new));
    }

    private static boolean isAnnotationPresent(Class<?> aClass) {
        return aClass.isAnnotationPresent(Service.class) || aClass.isAnnotationPresent(Repository.class);
    }

    private Set<Object> createBeans(Set<Class<?>> classes) {
        return classes.stream()
                .map(FunctionWrapper.apply(Class::getDeclaredConstructor))
                .peek(constructor -> constructor.setAccessible(true))
                .map(FunctionWrapper.apply(Constructor::newInstance))
                .collect(Collectors.toUnmodifiableSet());
    }

    private void setFields(Object bean) {
        Arrays.stream(bean.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .forEach(field -> assignBeanToField(bean, field));
    }

    private void assignBeanToField(Object bean, Field field) {
        field.setAccessible(true);

        Class<?> fieldType = field.getType();
        beans.stream()
                .filter(fieldType::isInstance)
                .forEach(ConsumerWrapper.accept(matchBean -> field.set(bean, matchBean)));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return beans.stream()
                .filter(aClass::isInstance)
                .findFirst()
                .map(bean -> (T) bean)
                .orElseThrow(() -> new IllegalArgumentException("bean을 찾을 수 없습니다."));
    }
}
