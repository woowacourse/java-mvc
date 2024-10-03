package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
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

    private DIContainer(Set<Class<?>> allClassesInPackage) throws Exception {
        for (Class<?> clazz : allClassesInPackage) {
            createAndStoreBean(clazz);
        }
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) throws Exception {
        Set<Class<?>> allClassesInPackage = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(allClassesInPackage);
    }

    private <T> void createAndStoreBean(Class<T> clazz) throws Exception {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);

        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length > 1) {
            throw new IllegalStateException("Fail to load interface for bean: " + clazz.getName());
        } else if (interfaces.length == 1) {
            beans.put(clazz.getInterfaces()[0], constructor.newInstance());
            return;
        }
        beans.put(clazz, constructor.newInstance());

    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) throws Exception {
        T instance = (T) beans.get(clazz);
        if (instance == null) {
            throw new IllegalStateException("No bean found for class: " + clazz.getName());
        }

        injectDependencies(instance);
        return instance;
    }

    private void injectDependencies(Object instance) throws Exception {
        List<Field> fields = Arrays.stream(instance.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .toList();
        for (Field field : fields) {
            field.setAccessible(true);
            field.set(instance, getBean(field.getType()));
        }
    }
}
