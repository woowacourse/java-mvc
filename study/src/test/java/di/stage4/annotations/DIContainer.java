package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import javax.annotation.Nullable;
import org.reflections.Reflections;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = new HashSet<>();
        initializeBean(classes);
        for (Object bean : beans) {
            injectFieldBean(bean);
        }
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Reflections reflections = new Reflections(rootPackageName);
        Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositoryClasses = reflections.getTypesAnnotatedWith(Repository.class);

        Set<Class<?>> beanClasses = new HashSet<>();
        beanClasses.addAll(serviceClasses);
        beanClasses.addAll(repositoryClasses);
        return new DIContainer(beanClasses);
    }

    private void initializeBean(Set<Class<?>> beanClasses) {
        Queue<Class<?>> queue = new LinkedList<>(beanClasses);
        while (!queue.isEmpty()) {
            Class<?> beanClass = queue.poll();
            Object bean = initializeBean(beanClass);
            if (bean == null) {
                queue.add(beanClass);
                continue;
            }
            beans.add(bean);
        }
    }

    private Object initializeBean(Class<?> beanClass) {
        Object bean = getBean(beanClass);
        if (bean != null) {
            return bean;
        }

        return createBean(beanClass);
    }

    private Object createBean(Class<?> beanClass) {
        try {
            Constructor<?>[] constructors = beanClass.getDeclaredConstructors();
            if (constructors.length == 0) {
                return null;
            }

            Constructor<?> constructor = constructors[0]; // 생성자는 1개만 있다고 가정
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] parameters = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                Object parameter = initializeBean(parameterTypes[i]);
                if (parameter == null) {
                    return null;
                }
                parameters[i] = parameter;
            }
            constructor.setAccessible(true);
            return constructor.newInstance(parameters);
        } catch (Exception e) {
            throw new RuntimeException("해당 빈을 만드는 데 실패했습니다." + beanClass.getName(), e);
        }
    }

    private void injectFieldBean(Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Inject.class)) {
                Object fieldBean = initializeBean(field.getType());
                try {
                    field.setAccessible(true);
                    field.set(bean, fieldBean);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("필드 주입에 실패했습니다: " + field.getName(), e);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> T getBean(final Class<T> aClass) {
        for (Object bean : beans) {
            if (aClass.isInstance(bean)) {
                return (T) bean;
            }
        }
        return null;
    }
}
