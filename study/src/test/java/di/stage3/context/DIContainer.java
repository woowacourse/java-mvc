package di.stage3.context;

import di.ConsumerWrapper;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DIContainer {

    private static final Logger log = LoggerFactory.getLogger(DIContainer.class);

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) throws Exception {
        beans = generateBeans(classes);
        injectFields(beans);
    }

    private Set<Object> generateBeans(Set<Class<?>> classes) throws Exception {
        Set<Object> beans = new LinkedHashSet<>();
        for (Class<?> aClass : classes) {
            Constructor<?> constructor = aClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object bean = constructor.newInstance();
            beans.add(bean);
        }
        return beans;
    }

    private void injectFields(Set<Object> beans) {
        beans.stream()
                .collect(Collectors.toMap(bean -> bean, bean -> bean.getClass().getDeclaredFields()))
                .forEach(this::injectField);
    }

    private void injectField(Object bean, Field[] fields) {
        for (Field field : fields) {
            setField(bean, field);
        }
    }

    private void setField(Object bean, Field field) {
        beans.stream()
                .filter(matchBean -> field.getType().isInstance(matchBean))
                .peek(matchBean -> field.setAccessible(true))
                .forEach(ConsumerWrapper.accept(matchBean -> field.set(bean, matchBean)));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(aClass::isInstance)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
