package di.stage4.annotations;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = classes.stream()
                .map(this::createInstance)
                .collect(Collectors.toSet());
        this.beans.forEach(this::injectFields);
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        return new DIContainer(ClassPathScanner.getAllClassesInPackage(rootPackageName));
    }

    private Object createInstance(final Class<?> clazz) {
        try {
            final var constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("빈 생성에 실패했습니다: " + clazz.getName(), e);
        }
    }

    private void injectFields(final Object bean) {
        for (final Field field : bean.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(Inject.class)) {
                continue;
            }
            final Object dependency = findBean(field.getType())
                    .orElseThrow(() -> new IllegalStateException(
                            "주입할 빈을 찾을 수 없습니다: " + field.getType().getName() + " → " + field));
            inject(bean, field, dependency);
        }
    }

    private <T> Optional<T> findBean(final Class<T> type) {
        return beans.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .findFirst();
    }

    private void inject(final Object bean, final Field field, final Object dependency) {
        try {
            field.setAccessible(true);
            field.set(bean, dependency);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("의존성 주입에 실패했습니다: " + field, e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return findBean(aClass)
                .orElseThrow(() -> new IllegalArgumentException("빈을 찾을 수 없습니다: " + aClass.getName()));
    }
}
