package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private static Logger log = LoggerFactory.getLogger(DIContainer.class);

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        this.beans.forEach(this::setFields);
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> allClassesInPackage = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(allClassesInPackage);
    }

    private Set<Object> createBeans(final Set<Class<?>> classes) {
        return classes.stream()
                .map(this::getDefaultConstructor)
                .collect(Collectors.toSet());
    }

    private Object getDefaultConstructor(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    private void setFields(final Object bean) {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        Arrays.stream(declaredFields)
                .forEach(field -> assignField(bean, field));
    }

    private void assignField(Object bean, Field field) {
        field.setAccessible(true);
        try {
            Object fieldBean = beans.stream()
                    .filter(aBean -> field.getType().isAssignableFrom(aBean.getClass()))
                    .findFirst()
                    .orElse(null);

            if (fieldBean != null) {
                field.set(bean, fieldBean);
            }
        } catch (IllegalAccessException e) {
            log.error("{}", e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(bean -> aClass.isAssignableFrom(bean.getClass()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
