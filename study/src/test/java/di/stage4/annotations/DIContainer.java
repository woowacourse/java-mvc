package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private static final Logger log = LoggerFactory.getLogger(DIContainer.class);

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = initBeans(classes);
    }

    private Set<Object> initBeans(final Set<Class<?>> targetClasses) {
        final Set<Object> beans = targetClasses.stream()
                .map(this::convertBean)
                .collect(Collectors.toSet());
        beans.forEach(bean -> injectionDependency(beans, bean));

        return beans;
    }

    private Object convertBean(final Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return null;
    }

    private void injectionDependency(final Set<Object> dependencies, final Object bean) {
        final List<Field> hasInjectionAnnotationFields = getHasInjectionAnnotationFields(bean);
        if (hasInjectionAnnotationFields.isEmpty()) {
            return;
        }

        hasInjectionAnnotationFields.forEach(field -> {
            final Object dependency = findDependency(dependencies, field);
            setDependency(dependency, bean, field);
        });
    }

    private List<Field> getHasInjectionAnnotationFields(final Object bean) {
        final Class<?> clazz = bean.getClass();
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .toList();
    }

    private Object findDependency(final Set<Object> dependencies, final Field field) {
        final Class<?> fieldType = field.getType();
        return dependencies.stream()
                .filter(dependency -> fieldType.isAssignableFrom(dependency.getClass()))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("의존성이 존재하지 않습니다."));
    }

    private void setDependency(
            final Object dependency,
            final Object bean,
            final Field field
    ) {
        try {
            field.setAccessible(true);
            field.set(bean, dependency);
        } catch (final Exception e) {
            log.error(e.getMessage());
        }
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        final Set<Class<?>> targetClasses = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(targetClasses);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        final Optional<Object> target = beans.stream()
                .filter(bean -> aClass.isAssignableFrom(bean.getClass()))
                .findAny();
        return target.map(bean -> (T) bean)
                .orElseThrow(() -> new NoSuchElementException("해당되는 Bean이 존재하지 않습니다."));
    }
}
