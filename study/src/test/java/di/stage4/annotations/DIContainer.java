package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
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
        this.beans = new HashSet<>();
        for (Class<?> aClass : classes) {
            try {
                Constructor<?> constructor = aClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                beans.add(constructor.newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        for (Object bean : beans) {
            Field[] declaredFields = bean.getClass().getDeclaredFields();
            List<Field> annotatedFields = Arrays.stream(declaredFields)
                    .filter(field -> field.isAnnotationPresent(Inject.class))
                    .toList();

            for (Field declaredField : annotatedFields) {
                declaredField.setAccessible(true);
                try {
                    for (Object o : beans) {
                        if (declaredField.getType().isAssignableFrom(o.getClass())) {
                            declaredField.set(bean, o);
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> allClassesInPackage = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(allClassesInPackage);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(bean -> aClass.isAssignableFrom(bean.getClass()))
                .findAny()
                .orElseThrow();
    }
}
