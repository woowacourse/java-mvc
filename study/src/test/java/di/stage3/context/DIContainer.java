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

    private final Map<Class<?>, Constructor<?>> beanConstructors = new HashMap<>();

    public DIContainer(Set<Class<?>> classes) throws Exception {
        for (Class<?> clazz : classes) {
            createAndStoreBean(clazz);
        }
    }

    private <T> void createAndStoreBean(Class<T> clazz) {
        List<Constructor<?>> constructors = Arrays.stream(clazz.getDeclaredConstructors())
                .sorted((c1, c2) -> Integer.compare(c2.getParameterCount(), c1.getParameterCount()))
                .toList();
        if (constructors.isEmpty()) {
            throw new IllegalStateException("Fail to find constructor for bean: " + clazz.getName());
        }
        Constructor<?> constructor = constructors.get(0);
        constructor.setAccessible(true);

        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length > 1) {
            throw new IllegalStateException("Fail to load interface for bean: " + clazz.getName());
        } else if (interfaces.length == 1) {
            this.beanConstructors.put(clazz.getInterfaces()[0], constructor);
            return;
        }
        this.beanConstructors.put(clazz, constructor);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) throws Exception {
        Constructor<?> constructor = beanConstructors.get(clazz);
        if (constructor == null) {
            throw new IllegalStateException("No constructor found for class: " + clazz.getName());
        }

        var parameterTypes = constructor.getParameterTypes();
        Object[] params = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            params[i] = getBean(parameterTypes[i]);
        }

        return (T) constructor.newInstance(params);
    }
}
