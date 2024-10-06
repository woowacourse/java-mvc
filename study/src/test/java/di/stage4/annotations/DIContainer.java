package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) throws Exception {
        this.beans = createBeans(classes);
        for (Object bean : this.beans) {
            setFields(bean);
        }
    }

    private Set<Object> createBeans(final Set<Class<?>> classes) throws Exception {
        Set<Object> beans = new HashSet<>();
        for (Class<?> aClass : classes) {
            Constructor<?> constructor = aClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            beans.add(constructor.newInstance());
        }
        return beans;
    }

    private void setFields(final Object bean) throws IllegalAccessException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Inject.class)) {
                setField(bean, field);
            }
        }
    }

    private void setField(Object bean, Field field) throws IllegalAccessException {
        for (Object fieldCandidate : beans) {
            Class<?> fieldType = field.getType();
            if (fieldType.isAssignableFrom(fieldCandidate.getClass())) {
                field.setAccessible(true);
                field.set(bean, getBean(fieldType));
            }
        }
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) throws Exception {
        Set<Class<?>> annotationClasses = ClassPathScanner.getAnnotationClasses(rootPackageName, Repository.class, Service.class);

        return new DIContainer(annotationClasses);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return beans.stream()
                .filter(bean -> aClass.isAssignableFrom(bean.getClass()))
                .findFirst()
                .map(bean -> (T) bean)
                .orElseThrow();
    }
}
