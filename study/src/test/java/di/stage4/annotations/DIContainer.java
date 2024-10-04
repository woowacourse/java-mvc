package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans = new HashSet<>();

    public DIContainer(final Set<Class<?>> classes) {
        initialize(classes);
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> classes = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(classes);
    }

    private void initialize(final Set<Class<?>> classes) {
        try {
            createInstances(classes);
            injectFieldsToBeans();
        } catch (Exception e) {
            throw new RuntimeException("cannot initialize DIContainer", e);
        }
    }

    private void createInstances(final Set<Class<?>> classes) throws Exception {
        for (Class<?> aClass : classes) {
            beans.add(createInstance(aClass));
        }
    }

    private Object createInstance(Class<?> aClass) throws Exception {
        Constructor<?> constructor = getNoArgsConstructor(aClass);
        return constructor.newInstance();
    }

    private Constructor<?> getNoArgsConstructor(Class<?> aClass) throws NoSuchMethodException {
        Constructor<?> declaredConstructor = aClass.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);
        return declaredConstructor;
    }

    private void injectFieldsToBeans() throws Exception {
        for (Object bean : beans) {
            injectFields(bean);
        }
    }

    private void injectFields(Object bean) throws Exception {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            injectField(bean, field);
        }
    }

    private void injectField(Object bean, Field field) throws Exception {
        field.setAccessible(true);
        Object dependency = getBean(field.getType());
        if (dependency != null) {
            field.set(bean, dependency);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        for (Object bean : beans) {
            if (aClass.isInstance(bean)) {
                return (T)bean;
            }
        }
        return null;
    }
}
