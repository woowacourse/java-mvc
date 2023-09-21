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
        this.beans.forEach(this::setFields);
    }

    private Set<Object> createBeans(Set<Class<?>> classes) throws Exception {
        Set<Object> beans = new HashSet<>();
        for (Class<?> aClass : classes) {
            Constructor<?> constructor = aClass.getDeclaredConstructor();
            constructor.setAccessible(true);
//            Object o = ReflectionUtils.newInstance(aClass);
//            Object o = ReflectionUtils.newInstance(constructor);
            Object o = constructor.newInstance();
            beans.add(o);
        }

        return beans;
    }

    private void setFields(Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            beans.stream()
                 .filter(field.getType()::isInstance)
                 .findFirst()
                 .ifPresent(o -> {
                     field.setAccessible(true);
                     try {
                         field.set(bean, o);
                     } catch (IllegalAccessException e) {
                         e.printStackTrace();
                     }
                 });
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                        .filter(bean -> bean.getClass().equals(aClass))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 클래스입니다."));
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> allClassesInPackage = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        try {
            return new DIContainer(allClassesInPackage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
