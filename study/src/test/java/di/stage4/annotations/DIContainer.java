package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    private DIContainer(final Set<Class<?>> classes) {
        this.beans = new HashSet<>();
        for (Class<?> clazz : classes) {
            try {
                Constructor<?> declaredConstructor = clazz.getDeclaredConstructor();
                declaredConstructor.setAccessible(true);
                Object instance = declaredConstructor.newInstance();
                beans.add(instance);
            } catch (Exception e) {
                throw new RuntimeException("Failed to instantiate class: " + clazz, e);
            }
        }

        for (Object bean : beans) {
            injectDependencies(bean);
        }
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> allClassesInPackage = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(allClassesInPackage);
    }

    private void injectDependencies(Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        Arrays.stream(fields)
                .forEach(field -> injectField(bean, field));
    }

    private void injectField(Object bean, Field field) {
        beans.stream()
                .filter(obj -> field.getType().isAssignableFrom(obj.getClass()))
                .findFirst()
                .ifPresent(obj -> setField(field, bean, obj));
    }

    private void setField(Field field, Object bean, Object obj) {
        try {
            field.setAccessible(true);
            field.set(bean, obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        for (Object bean : beans) {
            if (aClass.isInstance(bean)) {
                return aClass.cast(bean);
            }
        }
        throw new RuntimeException("No bean found for class: " + aClass.getName());
    }
}
