package di.stage3.context;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.junit.platform.commons.util.ReflectionUtils;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = instantiateBeans(classes);
        injectDependencies();
    }

    private Set<Object> instantiateBeans(final Set<Class<?>> classes) {
        return classes.stream()
                .map(ReflectionUtils::newInstance)
                .collect(Collectors.toSet());
    }

    private void injectDependencies() {
        beans.forEach(this::injectDependenciesIntoBean);
    }

    private void injectDependenciesIntoBean(Object bean) {
        List<Field> fields = List.of(bean.getClass().getDeclaredFields());
        fields.forEach(field -> injectDependency(bean, field));
    }

    private void injectDependency(Object bean, Field field) {
        beans.stream()
                .filter(isInjectableField(field))
                .findFirst()
                .ifPresent(dependency -> setField(bean, field, dependency));
    }

    private Predicate<Object> isInjectableField(Field field) {
        return dependency -> field.getType().isInstance(dependency);
    }

    private void setField(Object bean, Field field, Object dependency) {
        try {
            ReflectionUtils.makeAccessible(field);
            field.set(bean, dependency);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("의조성 주입대상: " + dependency + ", 의존성 주입 실패 field: " + field.getName(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(aClass::isInstance)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Bean을 찾을 수 없습니다. : " + aClass.getName()));
    }
}
