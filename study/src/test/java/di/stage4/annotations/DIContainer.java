package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.beans = new HashSet<>();
        for (Class<?> clazz : classes) {
            Constructor<?> declaredConstructor = clazz.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            Object instance = declaredConstructor.newInstance();
            beans.add(instance);
            injectDependencies(instance, beans);
        }
    }

    public static DIContainer createContainerForPackage(final String rootPackageName)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Set<Class<?>> allClassesInPackage = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(allClassesInPackage);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        for (Object bean : beans) {
            if (aClass.isInstance(bean)) {
                return (T) bean;
            }
        }
        return null;
    }

    private void injectDependencies(Object instance, Set<Object> beans) {
        Field[] declaredFields = instance.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(Inject.class)) {
                declaredField.setAccessible(true);
                for (Object bean : beans) {
                    if (declaredField.getType().isInstance(bean)) {
                        try {
                            declaredField.set(instance, bean);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Failed to inject dependency", e);
                        }
                    }
                }
            }
        }
    }
}
