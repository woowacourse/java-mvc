package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public static DIContainer createContainerForPackage(final String rootPackageName) throws Exception {
        Set<Class<?>> allClassesInPackage = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(allClassesInPackage);
    }

    public DIContainer(final Set<Class<?>> classes) throws Exception {
        this.beans = new LinkedHashSet<>();
        initBean(classes);
        fieldInjection();
    }

    private void initBean(Set<Class<?>> classes) throws Exception {
        for (Class<?> aClass : classes) {
            Constructor<?>[] constructors = aClass.getDeclaredConstructors();
            for (Constructor<?> constructor : constructors) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                if (countMatchedParameter(parameterTypes) == parameterTypes.length) {
                    constructor.setAccessible(true);
                    Object[] beanOf = getBeanOf(parameterTypes);
                    beans.add(constructor.newInstance(beanOf));
                    break;
                }
            }
        }
    }

    private int countMatchedParameter(Class<?>[] parameterTypes) {
        return (int) Arrays.stream(parameterTypes).filter(parameter -> getBean(parameter) != null).count();
    }

    private Object[] getBeanOf(Class<?>[] parameterTypes) {
        return Arrays.stream(parameterTypes).map(this::getBean).toArray();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream().filter(bean -> aClass.isAssignableFrom(bean.getClass())).findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private void fieldInjection() throws IllegalAccessException {
        for (Object bean : beans) {
            Field[] fields = bean.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Inject.class)) {
                    field.setAccessible(true);
                    field.set(bean, getBean(field.getType()));
                }
            }
        }
    }
}
