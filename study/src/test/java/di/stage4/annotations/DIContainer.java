package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import di.ConsumerWrapper;
import di.FunctionWrapper;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        beans = createBeans(classes);
        beans.forEach(this::setFields);
    }

    private Set<Object> createBeans(final Set<Class<?>> classes) {
        return classes.stream()
                .map(FunctionWrapper.apply(Class::getDeclaredConstructor))
                .peek(constructor -> constructor.setAccessible(true))
                .map(FunctionWrapper.apply(Constructor::newInstance))
                .collect(Collectors.toUnmodifiableSet());
    }

    private void setFields(final Object bean) {
        Arrays.stream(bean.getClass().getDeclaredFields())
                .forEach(field -> setFields(bean, field));
    }

    private void setFields(final Object bean, final Field field) {
        final var fieldType = field.getType();
        field.setAccessible(true);

        beans.stream()
                .filter(fieldType::isInstance)
                .forEach(ConsumerWrapper.accept(matchBean -> field.set(bean, matchBean)));
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> allClasses = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        Set<Class<?>> servicesAndRepositories = allClasses.stream()
                .filter(clazz -> clazz.isAnnotationPresent(Service.class) || clazz.isAnnotationPresent(Repository.class))
                .collect(Collectors.toUnmodifiableSet());
        return new DIContainer(servicesAndRepositories);
    }

    public <T> T getBean(final Class<T> aClass) {
        return beans.stream()
                .filter(aClass::isInstance)
                .findFirst()
                .map(aClass::cast)
                .orElseThrow();
    }
}
