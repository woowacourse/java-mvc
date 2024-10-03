package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        this.beans = new HashSet<>();
        for (Class<?> clazz : classes) {
            final Constructor<?> declaredConstructor = clazz.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            final Object instance = declaredConstructor.newInstance();
            beans.add(instance);
        }

        for (Object bean : beans) {
            Field[] declaredFields = bean.getClass().getDeclaredFields();
            List<Field> annotationFields = new ArrayList<>();
            for (final Field declaredField : declaredFields) {
                if (declaredField.isAnnotationPresent(Inject.class)) {
                    annotationFields.add(declaredField);
                }
            }
            for (Field field : annotationFields) {
                field.setAccessible(true);
                for (Object o : beans) {
                    if (field.getType().isAssignableFrom(o.getClass())) {
                        field.set(bean, o);
                    }
                }
            }
        }
    }

    public static DIContainer createContainerForPackage(final String rootPackageName)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        final Set<Class<?>> allClassesInPackage = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(allClassesInPackage);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(aClass::isInstance)
                .findAny()
                .orElseThrow(NoSuchElementException::new);
    }
}
