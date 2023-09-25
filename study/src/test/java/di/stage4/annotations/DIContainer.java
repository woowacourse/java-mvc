package di.stage4.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.platform.commons.util.ReflectionUtils;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private static final List<Class<? extends Annotation>> COMPONENT_CLASS_ANNOTATIONS = Arrays.asList(Service.class, Repository.class);

    private final Set<Object> beans;

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        final var classes = ClassPathScanner.getAllAnnotatedClassesInPackage(rootPackageName, COMPONENT_CLASS_ANNOTATIONS);
        return new DIContainer(classes);
    }

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        this.beans.forEach(this::setFields);
    }

    private Set<Object> createBeans(final Set<Class<?>> classes) {
        return classes.stream()
                      .map(ReflectionUtils::newInstance)
                      .collect(Collectors.toSet());
    }

    private void setFields(Object o) {
        final var clazz = o.getClass();
        Arrays.stream(clazz.getDeclaredFields())
              .filter(field -> field.isAnnotationPresent(Inject.class))
              .forEach(field -> setField(o, field));
    }

    private void setField(Object o, Field field) {
        field.setAccessible(true);
        try {
            field.set(o, getBean(field.getType()));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        final Object o = beans.stream()
                              .filter(aClass::isInstance)
                              .findFirst()
                              .orElseThrow(() -> new IllegalArgumentException("해당하는 클래스가 존재하지 않습니다."));
        return (T) o;
    }
}
