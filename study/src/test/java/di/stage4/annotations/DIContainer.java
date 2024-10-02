package di.stage4.annotations;

import di.ConsumerWrapper;
import di.FunctionWrapper;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        for (Object bean : beans) {
            setFields(bean);
        }
    }

    private Set<Object> createBeans(final Set<Class<?>> classes) {
        Set<Object> newInstances = new HashSet<>();
        for (Class<?> clazz : classes) {
            if (isBeanClass(clazz)) {
                Object newInstance = createBean(clazz);
                newInstances.add(newInstance);
            }
        }
        return newInstances;
    }

    private boolean isBeanClass(Class<?> clazz) {
        return clazz.isAnnotationPresent(Service.class) || clazz.isAnnotationPresent(Repository.class);
    }

    private Object createBean(final Class<?> clazz) {
        Constructor<?> constructor = getConstructor(clazz);
        return getNewInstance(constructor);
    }

    private Constructor<?> getConstructor(final Class<?> clazz) {
        Function<Object, Constructor<?>> constructorFunction = FunctionWrapper.apply(
                function -> clazz.getDeclaredConstructor());
        Constructor<?> constructor = constructorFunction.apply(clazz);
        constructor.setAccessible(true);
        return constructor;
    }

    private Object getNewInstance(Constructor<?> constructor) {
        Function<Object, Object> newInstanceFunction = FunctionWrapper.apply(
                function -> constructor.newInstance()
        );
        return newInstanceFunction.apply(constructor);
    }

    private void setFields(Object targetBean) {
        Field[] fields = targetBean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (isInjectableField(field)) {
                setField(targetBean, field);
            }
        }
    }

    private boolean isInjectableField(Field field) {
        return field.isAnnotationPresent(Inject.class);
    }

    private void setField(Object targetBean, Field field) {
        Optional<Object> injectableBean = findInjectableBeanForField(field);
        if (injectableBean.isEmpty()) {
            return;
        }
        injectField(targetBean, injectableBean.get(), field);
    }

    private Optional<Object> findInjectableBeanForField(Field field) {
        Class<?> fieldType = field.getType();
        for (Object candidateBean : beans) {
            if (fieldType.isInstance(candidateBean)) {
                return Optional.of(candidateBean);
            }
        }
        return Optional.empty();
    }

    private void injectField(Object targetBean, Object injectableBean, Field field) {
        field.setAccessible(true);
        Consumer<Object> fieldInjector = ConsumerWrapper.accept(
                bean -> field.set(targetBean, bean)
        );
        fieldInjector.accept(injectableBean);
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
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
        throw new IllegalArgumentException("존재하지 않는 bean 입니다: " + aClass.getName());
    }
}
