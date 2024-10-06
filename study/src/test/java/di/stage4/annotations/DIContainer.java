package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
        for (Class<?> beanClass : classes) {
            beans.add(createBean(beanClass));
        }
        for (Object bean : beans) {
            injectBeanAtField(bean);
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

    private Object createBean(Class<?> beanClass) {
        try {
            Constructor<?> constructor = beanClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("해당 빈을 만드는 데 실패했습니다." + beanClass.getName(), e);
        }
    }

    private void injectBeanAtField(Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Inject.class)) {
                continue;
            }
            Object fieldBean = getBean(field.getType());
            try {
                field.setAccessible(true);
                field.set(bean, fieldBean);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("필드 주입에 실패했습니다: " + field.getName(), e);
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
