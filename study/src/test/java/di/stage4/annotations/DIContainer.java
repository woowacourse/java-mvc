package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = new HashSet<>();
        for (final Class<?> clazz : classes) {
            try {
                final Constructor<?> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                beans.add(constructor.newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e + " Bean 등록 시 예외가 발생했습니다.");
            }
        }
        initialize(classes);
    }

    private void initialize(final Set<Class<?>> classes) {
        for (final Class<?> injectableClass : classes) {
            updateFields(injectableClass);
        }
    }

    private void updateFields(final Class<?> injectableClass) {
        try {
            setFields(injectableClass);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("필드에 접근할 수 없습니다.");
        }
    }

    private void setFields(final Class<?> injectableClass) throws IllegalAccessException {
        final Field[] fields = injectableClass.getDeclaredFields();
        for (final Field field : fields) {
            if (!field.isAnnotationPresent(Inject.class)) {
                continue;
            }
            final Object instance = getBean(injectableClass);
            field.setAccessible(true);
            field.set(instance, getBean(field.getType()));
        }
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        final Set<Class<?>> injectableClasses = ClassPathScanner.getAllClassesInPackage(rootPackageName);

        return new DIContainer(injectableClasses);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        if (aClass.isInterface()) {
            return (T) beans.stream()
                .filter(bean -> Arrays.asList(bean.getClass().getInterfaces()).contains(aClass))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("해당하는 클래스가 존재하지 않습니다."));
        }
        return (T) beans.stream()
            .filter(bean -> aClass.isAssignableFrom(bean.getClass()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("해당하는 클래스가 존재하지 않습니다."));
    }
}
