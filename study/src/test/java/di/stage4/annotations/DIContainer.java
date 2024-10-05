package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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

        Map<Field, Object> injectFields = findInjectFieldTypes(subClass).stream()
                .collect(Collectors.toMap(Function.identity(), field -> registerBean(field.getType(), classes)));

        return registerBean(subClass, injectFields);
    }

    private Object registerBean(Class<?> aClass, Map<Field, Object> injectFields) {
        try {
            Constructor<?> defaultConstructor = aClass.getDeclaredConstructor();
            defaultConstructor.setAccessible(true);
            Object bean = defaultConstructor.newInstance();
            injectFields.forEach((key, value) -> setBeanField(bean, key, value));
            beans.put(aClass, bean);
            return bean;
        } catch (Throwable e) {
            throw new IllegalStateException("객체를 생성하는데 실패하였습니다.");
        }
    }

    private void setBeanField(Object bean, Field field, Object fieldInstance) {
        field.setAccessible(true);
        try {
            field.set(bean, fieldInstance);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
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

    private List<Field> findInjectFieldTypes(Class<?> aClass) {
        return Arrays.stream(aClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .toList();
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> classes = ClassPathScanner.getAllClassesInPackage(rootPackageName).stream()
                .filter(DIContainer::isBeanClass)
                .collect(Collectors.toSet());
        return new DIContainer(classes);
    }

    private static boolean isBeanClass(Class<?> aClass) {
        return aClass.isAnnotationPresent(Repository.class) || aClass.isAnnotationPresent(Service.class);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.get(findSubClass(aClass, beans.keySet()));
    }
}
