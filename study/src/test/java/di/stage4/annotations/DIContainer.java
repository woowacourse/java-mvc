package di.stage4.annotations;

import di.ConsumerWrapper;
import di.FunctionWrapper;
import java.lang.reflect.Field;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        this.beans.forEach(bean -> setFields(bean, bean.getClass().getDeclaredFields()));
    }

    private Set<Object> createBeans(Set<Class<?>> classes) {
        return classes.stream()
                .map(FunctionWrapper.apply(clazz -> clazz.getDeclaredConstructor()))
                .peek(constructor -> constructor.setAccessible(true))
                .map(FunctionWrapper.apply(constructor -> constructor.newInstance()))
                .collect(Collectors.toSet());
    }

    private void setFields(final Object targetBean, final Field[] fields) {
        for (Field field : fields) {
            field.setAccessible(true);

            beans.stream()
                    .filter(bean -> field.getType().isInstance(bean))
                    .forEach(ConsumerWrapper.accept(fieldBean -> field.set(targetBean, fieldBean)));
        }
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> classes = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(classes);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> clazz) {
        return (T) beans.stream()
                .filter(bean -> clazz.isInstance(bean))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("no such bean exists."));
    }
}
