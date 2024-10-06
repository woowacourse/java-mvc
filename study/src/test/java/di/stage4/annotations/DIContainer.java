package di.stage4.annotations;

import di.ConsumerWrapper;
import di.FunctionWrapper;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        this.beans.forEach(this::setBeanFields);
    }

    private Set<Object> createBeans(Set<Class<?>> classes) {
        return classes.stream()
                .map(FunctionWrapper.apply(Class::getDeclaredConstructor))
                .peek(constructor -> constructor.setAccessible(true))
                .map(FunctionWrapper.apply(Constructor::newInstance))
                .collect(Collectors.toUnmodifiableSet());
    }

    private void setBeanFields(Object bean) {
        Field[] declaredFields = bean.getClass().getDeclaredFields();

        Stream.of(declaredFields)
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .forEach(field -> setField(bean, field));
    }

    private void setField(Object bean, Field field) {
        field.setAccessible(true);

        beans.stream()
                .filter(field.getType()::isInstance)
                .forEach(ConsumerWrapper.accept(matchBean -> field.set(bean, matchBean)));

        field.setAccessible(false);
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> classesInPackage = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(classesInPackage);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(aClass::isInstance)
                .findAny()
                .orElseThrow(() -> new NoSuchElementException(aClass.getSimpleName() + "클래스를 찾을 수 없습니다."));
    }
}
