package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        beans = instantiateBeans(classes);
    }

    private Set<Object> instantiateBeans(final Set<Class<?>> classes) {
        final Map<Class<?>, Object> classObjectMap = new HashMap<>();
        final List<Class<?>> sortedClasses = classes.stream()
                .sorted(Comparator
                        .comparing(a -> ((Class<?>) a).getDeclaredFields().length)
                        .reversed()
                )
                .collect(Collectors.toList());
        System.out.println("sortedClasses = " + sortedClasses);
        for (final Class<?> sortedClass : sortedClasses) {
            if (sortedClass.getConstructors().length != 0) {
                final Constructor<?> defaultConstructor = sortedClass.getConstructors()[0];
                if (defaultConstructor.getParameterCount() != 0) {
                    final Class<?>[] parameterTypes = defaultConstructor.getParameterTypes();
                    final Object[] parameters = new Object[parameterTypes.length];
                    for (int i = 0; i < parameterTypes.length; i++) {
                        parameters[i] = classObjectMap.get(parameterTypes[i]);
                    }
                    try {
                        putInterfaceOrClass(sortedClass, classObjectMap, defaultConstructor, parameters);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    final Constructor<?> constructor = sortedClass.getConstructors()[0];
                    try {
                        putInterfaceOrClass(sortedClass, classObjectMap, constructor);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            } else if (sortedClass.getDeclaredFields().length != 0) {
                Arrays.stream(sortedClass.getDeclaredFields())
                        .filter(field -> field.isAnnotationPresent(Inject.class))
                        .forEach(field -> {
                            try {
                                field.setAccessible(true);
                                final Constructor<?> declaredConstructor = sortedClass.getDeclaredConstructors()[0];
                                declaredConstructor.setAccessible(true);
                                final Object instance = declaredConstructor.newInstance();
                                field.set(instance, classObjectMap.get(field.getType()));
                                classObjectMap.put(sortedClass, instance);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });
            }
        }
        return new HashSet<>(classObjectMap.values());
    }

    private static void putInterfaceOrClass(final Class<?> sortedClass, final Map<Class<?>, Object> classObjectMap, final Constructor<?> constructor, final Object... parameters) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        final Class<?>[] interfaces = sortedClass.getInterfaces();
        for (final Class<?> anInterface : interfaces) {
            classObjectMap.put(anInterface, constructor.newInstance(parameters));
        }
        classObjectMap.put(sortedClass, constructor.newInstance(parameters));
    }

    public static DIContainer createContainerForPackage(final String packageName) {
        return new DIContainer(ClassPathScanner.getAllClassesInPackage(packageName));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        System.out.println("beans = " + beans);
        return (T) beans.stream()
                .peek(System.out::println)
                .filter(bean -> aClass.isAssignableFrom(bean.getClass()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("해당하는 클래스가 존재하지 않습니다."));
    }
}
