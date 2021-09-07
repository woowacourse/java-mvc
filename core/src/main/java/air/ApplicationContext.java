package air;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.google.common.base.CaseFormat;

import air.annotation.*;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationContext {

    private static final Logger log = LoggerFactory.getLogger(ApplicationContext.class);

    private static final String[] basePackages = {"air", "com.techcourse", "nextstep"};
    private static final Map<String, Object> beans = new ConcurrentHashMap<>();

    public void initializeContext() {
        Set<Class<?>> components = componentScan();
        for (Class<?> component : components) {
            Object bean = getBean(component);
            String beanName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, component.getSimpleName());
            beans.put(beanName, bean);
        }

        registerAllConfigurationBeans();

        log.info("============ Success bean register!");
    }

    private Set<Class<?>> componentScan() {
        Set<Class<?>> allComponents = new HashSet<>();
        for (String basePackage : basePackages) {
            Reflections reflections = new Reflections(basePackage, new SubTypesScanner(false));

            Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class);
            Set<Class<?>> components = allClasses.stream()
                                                 .filter(this::isComponent)
                                                 .collect(Collectors.toSet());

            allComponents.addAll(components);
        }

        return allComponents;
    }

    private void registerAllConfigurationBeans() {
        for (String basePackage : basePackages) {
            Reflections reflections = new Reflections(basePackage);
            Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Configuration.class);
            registerConfigurationBeans(classes);
        }
    }

    private void registerConfigurationBeans(Set<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            List<Method> methods = findBeanMethods(clazz);

            methods.forEach(method -> {
                try {
                    String beanName = method.getName();
                    if (!beans.containsKey(beanName)) {
                        Object bean = method.invoke(clazz.getConstructor().newInstance());
                        beans.put(beanName, bean);
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
                    throw new RuntimeException();
                }
            });
        }
    }

    private List<Method> findBeanMethods(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                     .filter(method -> method.isAnnotationPresent(Bean.class))
                     .collect(Collectors.toList());
    }

    private boolean isComponent(Class<?> clazz) {
        return isComponentFriends(clazz) && !clazz.isAnnotation();
    }

    private boolean isComponentFriends(Class<?> clazz) {
        return clazz.isAnnotationPresent(Component.class)
                || clazz.isAnnotationPresent(Controller.class)
                || clazz.isAnnotationPresent(Configuration.class);
    }

    private Object getBean(Class<?> clazz) {
        if (beans.containsKey(clazz.getSimpleName())) {
            return beans.get(clazz.getSimpleName());
        }
        return createBean(clazz);
    }

    private Object createBean(Class<?> clazz) {
        try {
            Constructor<?> constructor = findAutowiredConstructor(clazz);
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] fields = getParametersForInjection(parameterTypes);
            return constructor.newInstance(fields);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException();
        }
    }

    private Constructor<?> findAutowiredConstructor(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredConstructors())
                     .filter(c -> c.isAnnotationPresent(Autowired.class))
                     .findAny()
                     .orElseGet(() -> {
                         try {
                             return clazz.getConstructor();
                         } catch (NoSuchMethodException e) {
                             throw new RuntimeException();
                         }
                     });
    }

    private Object[] getParametersForInjection(Class<?>[] parameterTypes) {
        Object[] fields = new Object[parameterTypes.length];
        int index = 0;
        for (Class<?> parameter : parameterTypes) {
            Object bean = findBeanByType(parameter);
            fields[index++] = bean;
        }
        return fields;
    }

    public static <T> T findBeanByType(Class<T> type) {
        for (String beanName : beans.keySet()) {
            Object bean = beans.get(beanName);
            if (type.isInstance(bean)) {
                return (T) bean;
            }
        }
        return registerBean(type);
    }

    private static <T> T registerBean(Class<T> type) {
        try {
            T instance = type.getConstructor().newInstance();
            String beanName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, type.getSimpleName());
            beans.put(beanName, instance);
            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException();
        }
    }
}
