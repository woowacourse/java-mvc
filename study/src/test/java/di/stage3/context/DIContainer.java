package di.stage3.context;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        beanMapper.put(aClass, bean);
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

    private Constructor<?> getConstructor(Class<?> aClass) {
        Constructor<?>[] constructors = aClass.getConstructors();
        if (constructors.length != 1) {
            throw new IllegalStateException("생성자를 확인해주세요");
        }
        return constructors[0];
    }

    private Object createInstance(Constructor<?> constructor, List<Object> parameters) {
        try {
            return constructor.newInstance(parameters.toArray());
        } catch (Exception e) {
            throw new IllegalStateException("빈을 생성하는데 실패했습니다.");
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beanMapper.get(aClass);
    }
}
