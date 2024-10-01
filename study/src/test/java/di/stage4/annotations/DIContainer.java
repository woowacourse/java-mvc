package di.stage4.annotations;

import di.ConsumerWrapper;
import di.FunctionWrapper;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = getBeans(classes);
    }

    private Set<Object> getBeans(Set<Class<?>> classes) {
        return classes.stream()
                .map(FunctionWrapper.apply(Class::getDeclaredConstructor))
                .peek(constructor -> constructor.setAccessible(true))
                .map(FunctionWrapper.apply(Constructor::newInstance))
                .map(FunctionWrapper.apply(instance -> getBean(instance, classes)))
                .collect(Collectors.toUnmodifiableSet());
    }

    private Object getBean(Object instance, Set<Class<?>> classes) {
        Field[] fields = instance.getClass().getDeclaredFields();
        Arrays.stream(fields)
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .peek(ConsumerWrapper.accept(field -> field.setAccessible(true)))
                .forEach(ConsumerWrapper.accept(field -> field.set(instance, findField(field, classes))));
        return instance;
    }

    private Object findField(Field field, Set<Class<?>> classes) {
        return classes.stream()
                .filter(clazz -> field.getType().isAssignableFrom(clazz))
                .findFirst()
                .map(FunctionWrapper.apply(Class::getDeclaredConstructor))
                .map(FunctionWrapper.apply(Constructor::newInstance))
                .orElseThrow(() -> new NoSuchElementException("주입 가능한 빈 없음"));
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Reflections reflections = new Reflections(rootPackageName);
        Set<Class<?>> beanClasses = new HashSet<>();
        beanClasses.addAll(reflections.getTypesAnnotatedWith(Repository.class));
        beanClasses.addAll(reflections.getTypesAnnotatedWith(Service.class));
        beanClasses.addAll(reflections.getTypesAnnotatedWith(Inject.class));
        return new DIContainer(beanClasses);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(bean -> aClass.isAssignableFrom(bean.getClass()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("빈 없음"));
    }
}
