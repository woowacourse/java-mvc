package air.context;

import java.lang.annotation.Annotation;
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

    private static final Map<Class<?>, Object> DEFAULT_VALUES;
    private static final List<String> BASE_PACKAGES = new ArrayList<>();
    private static final Map<String, Object> BEANS = new ConcurrentHashMap<>();

    static {
        DEFAULT_VALUES = Map.of(
                boolean.class, false,
                byte.class, (byte) 0,
                short.class, (short) 0,
                int.class, 0,
                long.class, (long) 0
        );
    }

    public ApplicationContext(String... basePackages) {
        BASE_PACKAGES.addAll(Arrays.asList(basePackages));
    }

    public void initializeContext() {
        Set<Class<?>> components = componentScan();
        for (Class<?> component : components) {
            Object bean = getBean(component);
            String beanName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, component.getSimpleName());
            BEANS.put(beanName, bean);
        }

        registerAllConfigurationBeans();

        log.info("============ Success bean register!");
    }

    private Set<Class<?>> componentScan() {
        Set<Class<?>> allComponents = new HashSet<>();
        for (String basePackage : BASE_PACKAGES) {
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
        for (String basePackage : BASE_PACKAGES) {
            Reflections reflections = new Reflections(basePackage);
            Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Configuration.class);
            registerConfigurationBeans(classes);
        }
    }

    private void registerConfigurationBeans(Set<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            List<Method> methods = findAllMethodByAnnotation(clazz, Bean.class);

            methods.forEach(method -> {
                try {
                    String beanName = method.getName();
                    if (!BEANS.containsKey(beanName)) {
                        Object bean = method.invoke(clazz.getConstructor().newInstance());
                        BEANS.put(beanName, bean);
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
                    e.printStackTrace();
                    throw new RuntimeException();
                }
            });
        }
    }

    public List<Method> findAllMethodByAnnotation(Class<?> clazz, Class<? extends Annotation> type) {
        return Arrays.stream(clazz.getDeclaredMethods())
                     .filter(method -> method.isAnnotationPresent(type))
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
        if (BEANS.containsKey(clazz.getSimpleName())) {
            return BEANS.get(clazz.getSimpleName());
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
            if (parameter.isPrimitive()) {
                fields[index++] = DEFAULT_VALUES.get(parameter);
                continue;
            }
            Object bean = findBeanByType(parameter);
            fields[index++] = bean;
        }
        return fields;
    }

    public <T> T findBeanByType(Class<T> type) {
        for (String beanName : BEANS.keySet()) {
            Object bean = BEANS.get(beanName);
            if (type.isInstance(bean)) {
                return (T) bean;
            }
        }
        String beanName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, type.getSimpleName());
        T bean = (T) createBean(type);
        BEANS.put(beanName, bean);
        return bean;
    }

    public List<Object> findAllBeanHasAnnotation(Class<? extends Annotation> type) {
        List<Object> result = new ArrayList<>();
        for (String key : BEANS.keySet()) {
            Object bean = BEANS.get(key);
            Class<?> beanClass = bean.getClass();
            if (beanClass.isAnnotationPresent(type)) {
                result.add(bean);
            }
        }
        return result;
    }
}
