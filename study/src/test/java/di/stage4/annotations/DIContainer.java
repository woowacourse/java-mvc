package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        beans = new HashSet<>();
        initBeans(classes);
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> allClassesInPackage = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(allClassesInPackage);
    }

    private void initBeans(Set<Class<?>> beanClasses) {
        for (Class<?> beanClass : beanClasses) {
            Constructor<?> basicConstructor = findConstructor(beanClass);
            int parameterCount = basicConstructor.getParameterCount();
            addBean(basicConstructor, beans, parameterCount);
        }
        for (Class<?> beanClass : beanClasses) {
            setFields(beanClass);
        }
    }

    private Constructor<?> findConstructor(Class<?> beanClass) {
        Constructor<?>[] constructors = beanClass.getDeclaredConstructors();
        return Arrays.stream(constructors)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("생성자가 없어서 객체를 생성할 수 없습니다."));
    }

    private void addBean(Constructor<?> basicConstructor, Set<Object> beans, int parameterCount) {
        try {
            Object[] parameters = new Object[parameterCount];
            basicConstructor.setAccessible(true);
            Object o = basicConstructor.newInstance(parameters);
            beans.add(o);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void setFields(Class<?> beanClass) {
        Object bean = getBean(beanClass);
        List<Field> fieldsToInject = Arrays.stream(beanClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .toList();
        for (Field field : fieldsToInject) {
            Class<?> type = field.getType();
            try {
                Object fieldObject = getBean(type);
                setField(field, bean, fieldObject);
            } catch (Exception ignored) {
            }
        }
    }

    private void setField(Field field, Object bean, Object fieldObject) {
        try {
            field.setAccessible(true);
            field.set(bean, fieldObject);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream().filter(bean -> aClass.isAssignableFrom(bean.getClass()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("해당 bean이 없습니다."));
    }
}
