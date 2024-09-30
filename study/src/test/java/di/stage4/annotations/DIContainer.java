package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        Set<Object> beans = new HashSet<>();
        for (Class<?> clazz : classes) {
            addInstance(clazz, classes, beans);
        }

        this.beans = beans;
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        return new DIContainer(ClassPathScanner.getAllClassesInPackage(rootPackageName));
    }

    private Object addInstance(Class<?> clazz, Set<Class<?>> classes, Set<Object> beans) {
        if (beans.stream().anyMatch(instance -> instance.getClass().equals(clazz))) {
            return beans.stream()
                    .filter(instance -> instance.getClass().equals(clazz))
                    .findFirst()
                    .get();
        }

        try {
            Constructor<?> constructor= clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object instance = constructor.newInstance();
            constructor.setAccessible(false);

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (!field.isAnnotationPresent(Inject.class)) continue;
                System.out.println("field = " + field.getName());
                Class<?> fieldClass = classes.stream()
                        .filter(c -> field.getType().isAssignableFrom(c))
                        .findFirst()
                        .orElseThrow();

                Object fieldInstance = addInstance(fieldClass, classes, beans);

                field.setAccessible(true);
                field.set(instance, fieldInstance);
                field.setAccessible(false);
            }
            beans.add(instance);
            return instance;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(instance -> instance.getClass().equals(aClass))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No beans exist for " + aClass.getName() +" in this container"));
    }
}
