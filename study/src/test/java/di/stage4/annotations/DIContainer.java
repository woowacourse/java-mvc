package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */

class DIContainer {

    private final Map<Class<?>, Class<?>> interfaceMapper = new HashMap<>();
    private final Map<Class<?>, Object> beanMapper = new HashMap<>();

    public DIContainer(final Set<Class<?>> classes) {
        mapInterfaceToImplementation(classes);
        classes.forEach(this::createBean);
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> allClassesInPackage = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(allClassesInPackage);
    }

    private void mapInterfaceToImplementation(Set<Class<?>> classes) {
        for (Class<?> aClass : classes) {
            interfaceMapper.put(aClass, aClass);
            Class<?>[] interfaces = aClass.getInterfaces();
            for (Class<?> aInterface : interfaces) {
                interfaceMapper.put(aInterface, aClass);
            }
        }
    }

    private void createBean(Class<?> aClass) {
        if (beanMapper.containsKey(aClass)) {
            return;
        }
        Constructor<?> constructor = getConstructor(aClass);
        List<Object> parameters = getParameters(constructor);
        Object bean = createInstance(constructor, parameters);
        injectByField(aClass, bean);
        beanMapper.put(aClass, bean);
    }

    private Constructor<?> getConstructor(Class<?> aClass) {
        Constructor<?>[] constructors = aClass.getConstructors();
        if (constructors.length == 0) {
            return getDefaultConstructor(aClass);
        }
        if (constructors.length == 1) {
            return constructors[0];
        }
        throw new IllegalStateException("public 생성자가 여러개입니다.");
    }

    private Constructor<?> getDefaultConstructor(Class<?> aClass) {
        try {
            Constructor<?> defaultConstructor = aClass.getDeclaredConstructor();
            defaultConstructor.setAccessible(true);
            return defaultConstructor;
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Object> getParameters(Constructor<?> constructor) {
        List<Object> parameters = new ArrayList<>();
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        for (Class<?> parameterType : parameterTypes) {
            Class<?> mappedParameterType = interfaceMapper.get(parameterType);
            createBean(mappedParameterType);
            parameters.add(beanMapper.get(mappedParameterType));
        }
        return parameters;
    }

    private Object createInstance(Constructor<?> constructor, List<Object> parameters) {
        try {
            return constructor.newInstance(parameters.toArray());
        } catch (Exception e) {
            throw new IllegalStateException("빈을 생성하는데 실패했습니다.");
        }
    }

    private void injectByField(Class<?> aClass, Object bean) {
        List<Field> fields = Arrays.stream(aClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .collect(Collectors.toList());
        for (Field field : fields) {
            Class<?> fieldType = field.getType();
            Class<?> mappedFieldType = interfaceMapper.get(fieldType);
            createBean(mappedFieldType);
            inject(bean, field, beanMapper.get(mappedFieldType));
        }
    }

    private void inject(Object bean, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(bean, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("필드 주입에 실패했습니다.");
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beanMapper.get(aClass);
    }
}
