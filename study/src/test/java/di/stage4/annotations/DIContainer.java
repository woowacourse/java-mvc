package di.stage4.annotations;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = new HashSet<>();
        classes.forEach(beanClass -> beans.add(createBean(beanClass)));
        beans.forEach(this::injectFields);
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        return new DIContainer(ClassPathScanner.getAllClassesInPackage(rootPackageName));
    }

    public <T> T getBean(final Class<T> aClass) {
        return beans.stream()
                .filter(aClass::isInstance)
                .map(aClass::cast)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No bean for " + aClass.getName()));
    }

    private Object createBean(final Class<?> beanClass) {
        try {
            final var constructor = beanClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Failed to create bean: " + beanClass.getName(), exception);
        }
    }

    private void injectFields(final Object bean) {
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(Inject.class)) {
                continue;
            }
            try {
                field.setAccessible(true);
                field.set(bean, getBean(field.getType()));
            } catch (IllegalAccessException exception) {
                throw new IllegalStateException("Failed to inject field: " + field, exception);
            }
        }
    }
}
