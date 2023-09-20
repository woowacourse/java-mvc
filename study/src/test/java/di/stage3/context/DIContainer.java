package di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.platform.commons.util.ReflectionUtils;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

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
        Arrays.stream(clazz.getConstructors())
              .filter(constructor -> constructor.getParameterCount() > 0)
              .findFirst()
              .ifPresent(constructor -> setFieldsByConstructor(o, constructor));
    }

    private void setFieldsByConstructor(Object o, Constructor<?> constructor) {
        final var parameters = Arrays.asList(constructor.getParameters());
        initializeFieldsByParameters(o, parameters, constructor.getDeclaringClass());
    }

    private void initializeFieldsByParameters(Object o, List<Parameter> parameters, Class<?> clazz) {
        final var fields = getMatchedFields(parameters, clazz);

        for (Field field : fields) {
            setField(o, field);
        }
    }

    private List<Field> getMatchedFields(List<Parameter> parameters, Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                     .filter(field -> parameters.stream().anyMatch(parameter -> parameter.getType().equals(field.getType())))
                     .collect(Collectors.toList());
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
