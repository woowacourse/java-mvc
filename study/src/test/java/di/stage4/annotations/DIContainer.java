package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {


    private final Set<Object> beans = new HashSet<>();
    private final Map<Class<?>, Object> beanBySuperTypeMap = new HashMap<>();

    public DIContainer(final Set<Class<?>> classes) {
        for (Class<?> aClass : classes) {
            createBean(aClass);
        }
        for (Object bean : beans) {
            injectBean(bean);
        }
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> allClassesInPackage = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        Set<Class<?>> beanCandidate = findBeanCandidate(allClassesInPackage);
        return new DIContainer(beanCandidate);
    }

    private static Set<Class<?>> findBeanCandidate(Set<Class<?>> allClassesInPackage) {
        return allClassesInPackage.stream()
                .filter(it -> it.isAnnotationPresent(Repository.class)
                        || it.isAnnotationPresent(Service.class))
                .collect(Collectors.toSet());
    }

    private void createBean(Class<?> aClass) {
        if (beanBySuperTypeMap.containsKey(aClass)) {
            return;
        }
        if (aClass.isInterface()) {
            createInterfaceBean(aClass);
            return;
        }
        Constructor<?> declaredConstructor = aClass.getDeclaredConstructors()[0];
        declaredConstructor.setAccessible(true);
        if (isDefaultConstructor(declaredConstructor)) {
            createBeanWithDefaultCtor(declaredConstructor);
        } else {
            createBeanWithCtorParameters(declaredConstructor);
        }
    }

    private void createInterfaceBean(Class<?> aClass) {
        Set<Class<?>> subTypes = findAllSubTypes(aClass);
        for (Class<?> subType : subTypes) {
            createBean(subType);
            return;
        }
    }

    private Set<Class<?>> findAllSubTypes(Class<?> aClass) {
        return (Set<Class<?>>)
                new Reflections(aClass.getPackage().getName()).getSubTypesOf(aClass);
    }

    private boolean isDefaultConstructor(Constructor<?> declaredConstructor) {
        Parameter[] parameters = declaredConstructor.getParameters();
        return parameters.length == 0;
    }

    private void createBeanWithDefaultCtor(Constructor<?> declaredConstructor) {
        try {
            Object o = declaredConstructor.newInstance();
            storeBean(o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void storeBean(Object o) {
        beanBySuperTypeMap.put(o.getClass(), o);
        if (o.getClass().getInterfaces().length != 0) {
            beanBySuperTypeMap.put(o.getClass().getInterfaces()[0], o);
        }
        beans.add(o);
    }

    private void createBeanWithCtorParameters(Constructor<?> declaredConstructor) {
        Parameter[] parameters = declaredConstructor.getParameters();
        Object[] parameterBeans = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Class<?> type = parameters[i].getType();
            createBean(type);
            parameterBeans[i] = getBean(type);
        }
        try {
            Object o = declaredConstructor.newInstance(parameterBeans);
            storeBean(o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void injectBean(Object bean) {
        for (Field field : bean.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (!field.isAnnotationPresent(Inject.class)) {
                continue;
            }
            try {
                field.set(bean, getBean(field.getType()));
            } catch (IllegalAccessException e) {
                System.out.println("Inject 오류!");
                throw new RuntimeException(e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beanBySuperTypeMap.get(aClass);
    }
}
