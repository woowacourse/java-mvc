package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
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
        Set<Class<?>> classes = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(classes);
    }

    private Set<Object> createBeans(Set<Class<?>> classes) {
        return classes.stream()
                .map(clazz -> {
                    try {
                        Constructor<?> constructor = clazz.getDeclaredConstructor();
                        constructor.setAccessible(true);
                        return constructor.newInstance();
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Bean Constructor Exception !!");
                    }
                })
                .collect(Collectors.toSet());
    }

    private void setFields(Object bean) {
        List<Field> fields = getToInjectFields(bean);
        fields.forEach(field -> field.setAccessible(true));

        for (Field field : fields) {
            Class<?> type = field.getType();
            try {
                Object injectField = beans.stream()
                        .filter(type::isInstance)
                        .findAny()
                        .orElse(null);
                if (injectField != null) {
                    field.set(bean, injectField);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Field Initialize Exception !!");
            }
        }
    }

    private List<Field> getToInjectFields(Object bean) {
        return Arrays.stream(bean.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(aClass::isInstance)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("no such bean"));
    }
}
