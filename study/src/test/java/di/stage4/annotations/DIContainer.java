package di.stage4.annotations;

import java.lang.reflect.Constructor;
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
                .sorted(Comparator.comparing(a -> a.getConstructors()[0].getParameterCount()))
                .collect(Collectors.toList());
        System.out.println("sortedClasses = " + sortedClasses);
        sortedClasses.forEach(aClass -> {
            final Constructor<?>[] constructors = aClass.getConstructors();
            Arrays.stream(constructors).forEachOrdered(constructor -> instantiateBean(aClass, constructor, classObjectMap));
        });
        return new HashSet<>(classObjectMap.values());
    }

    private static void instantiateBean(final Class<?> aClass, final Constructor<?> constructor, final Map<Class<?>, Object> classObjectMap) {
        if (constructor.getParameterCount() == 0) {
            try {
                putInterfacesToInstanceEntry(aClass, constructor, classObjectMap);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            findDependencyAndInstantiateBean(aClass, constructor, classObjectMap);
        }
    }

    private static void findDependencyAndInstantiateBean(final Class<?> aClass, final Constructor<?> constructor, final Map<Class<?>, Object> classObjectMap) {
        final Class<?>[] parameterTypes = constructor.getParameterTypes();
        final Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            final Class<?> parameterType = parameterTypes[i];
            final Object parameter = classObjectMap.get(parameterType);
            parameters[i] = parameter;
        }
        try {
            putInterfacesToInstanceEntry(aClass, constructor, classObjectMap, parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void putInterfacesToInstanceEntry(final Class<?> aClass, final Constructor<?> constructor, final Map<Class<?>, Object> classObjectMap, final Object... parameters) throws Exception {
        final Class<?>[] interfaces = aClass.getInterfaces();
        final Object instance = constructor.newInstance(parameters);
        if (interfaces.length == 0) {
            classObjectMap.put(aClass, instance);
        } else {
            for (final Class<?> anInterface : interfaces) {
                classObjectMap.put(anInterface, instance);
            }
        }
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        final Set<Class<?>> allClassesInPackage = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(allClassesInPackage);
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
