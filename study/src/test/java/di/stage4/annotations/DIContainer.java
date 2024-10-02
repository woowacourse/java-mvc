package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        this.beans.forEach(this::setFields);
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> allClassesInPackage = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(allClassesInPackage);
    }

    private Set<Object> createBeans(final Set<Class<?>> classes) {
        Set<Object> beans = new HashSet<>();
        for (Class<?> clazz : classes) {
            try {
                Constructor<?> declaredConstructor = clazz.getDeclaredConstructor();
                declaredConstructor.setAccessible(true);
                beans.add(declaredConstructor.newInstance());
            } catch (Exception e) {
                throw new RuntimeException("Failed to create bean for class: " + clazz.getName(), e);
            }
        }
        return beans;
    }

    private void setFields(final Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object dependency = getBean(field.getType());
            if (Objects.nonNull(dependency)) {
                field.setAccessible(true);
                try {
                    field.set(bean, dependency);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to inject dependency into field: " + field.getName(), e);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(b -> aClass.isAssignableFrom(b.getClass()))
                .findFirst()
                .orElse(null);
    }
}
