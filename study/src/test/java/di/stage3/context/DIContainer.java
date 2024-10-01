package di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = classes.stream()
                .map(aClass -> instantiate(aClass, classes))
                .collect(Collectors.toSet());
    }

    private static Object instantiate(Class<?> aClass, Set<Class<?>> classes) {
        Constructor<?>[] constructors = aClass.getDeclaredConstructors();
        return Arrays.stream(constructors)
                .filter(constructor -> Modifier.isPublic(constructor.getModifiers()))
                .filter(constructor -> canInstantiate(constructor, classes))
                .map(constructor -> createInstance(constructor, classes))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("인스턴스화를 위한 클래스가 존재하지 않습니다."));
    }

    private static boolean canInstantiate(Constructor<?> constructor, Set<Class<?>> classes) {
        return Arrays.stream(constructor.getParameterTypes())
                .allMatch(clazz -> checkContainsType(clazz, classes));
    }

    private static boolean checkContainsType(Class<?> clazz, Set<Class<?>> classes) {
        return classes.stream()
                .anyMatch(clazz::isAssignableFrom);
    }

    private static Object createInstance(Constructor<?> constructor, Set<Class<?>> classes) {
        try {
            Object[] parameterArray = Arrays.stream(constructor.getParameterTypes())
                    .map(aClass -> find(aClass, classes))
                    .map(aClass -> instantiate(aClass, classes))
                    .toArray();
            return constructor.newInstance(parameterArray);
        } catch (Exception e) {
            throw new IllegalStateException("인스턴스 생성에 실패했습니다.", e);
        }
    }

    private static Class<?> find(Class<?> clazz, Set<Class<?>> classes) {
        return classes.stream()
                .filter(clazz::isAssignableFrom)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 클래스입니다."));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(aClass::isInstance)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 클래스입니다."));
    }
}
