package di.stage3.context;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Map<Class<?>, Object> beans = new HashMap<>();

    public DIContainer(final Set<Class<?>> classes) {
        classes.forEach(aClass -> registerBean(aClass, classes));
    }

    private Object registerBean(final Class<?> aClass, Set<Class<?>> classes) {
        Class<?> subClass = findSubClass(aClass, classes);

        if (beans.containsKey(subClass)) {
            return beans.get(subClass);
        }

        List<Object> parameterObjects = Arrays.stream(findConstructorParameterTypes(subClass))
                .map(parameterType -> registerBean(parameterType, classes))
                .toList();

        return registerBean(subClass, parameterObjects);
    }

    private Object registerBean(Class<?> aClass, List<Object> parameterObjects) {
        try {
            Object bean = aClass.getConstructor(findConstructorParameterTypes(aClass))
                    .newInstance(parameterObjects.toArray());
            beans.put(aClass, bean);
            return bean;
        } catch (Throwable e) {
            throw new IllegalStateException("객체를 생성하는데 실패하였습니다.");
        }
    }

    private Class<?> findSubClass(final Class<?> aClass, Set<Class<?>> classes) {
        List<Class<?>> subClasses = classes.stream()
                .filter(aClass::isAssignableFrom)
                .toList();

        if (subClasses.size() != 1) {
            throw new IllegalStateException(aClass.getName() + " 타입의 빈이 여러개 등록되어 있습니다.");
        }

        return subClasses.get(0);
    }

    private Class<?>[] findConstructorParameterTypes(Class<?> aClass) {
        Constructor<?>[] constructors = aClass.getConstructors();
        if (constructors.length != 1) {
            throw new IllegalArgumentException(aClass.getName() + "의 생성자가 하나 이상이라 빈 등록에 실패하였습니다.");
        }

        return constructors[0].getParameterTypes();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.get(findSubClass(aClass, beans.keySet()));
    }
}
