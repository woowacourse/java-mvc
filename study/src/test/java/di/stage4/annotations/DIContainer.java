package di.stage4.annotations;

import di.ConsumerWrapper;
import di.FunctionWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
// stage3
class DIContainer {

    private static final Logger log = LoggerFactory.getLogger(DIContainer.class);
    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        this.beans.forEach(this::setFields);
    }

    // 기본 생성자로 빈을 생성한다.
    private Set<Object> createBeans(final Set<Class<?>> classes) {
        return classes.stream()
                .map(FunctionWrapper.apply(Class::getDeclaredConstructor))
                .peek(ConsumerWrapper.accept(constructor -> constructor.setAccessible(true)))
                .map(FunctionWrapper.apply(Constructor::newInstance))
                .collect(Collectors.toSet());
    }

    // 빈 내부에 선언된 필드를 각각 셋팅한다.
    // 각 필드에 빈을 대입(assign)한다.
    private void setFields(final Object bean) {
        final Field[] fields = bean.getClass().getDeclaredFields();
        Arrays.stream(fields)
                .peek(ConsumerWrapper.accept(field -> field.setAccessible(true)))
                .filter(field -> {
                    try {
                        return !Objects.nonNull(field.get(bean));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .forEach(ConsumerWrapper.accept(field -> field.set(bean, getBean(field.getType()))));
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
        Set<Class<?>> classes = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        Set<Class<?>> filteredClasses = classes.stream()
                .filter(clazz -> clazz.isAnnotationPresent(Service.class) ||
                        clazz.isAnnotationPresent(Repository.class))
                .collect(Collectors.toSet());
        return new DIContainer(filteredClasses);
    }
}

