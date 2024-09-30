package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
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

    // 기본 생성자로 빈을 생성한다.
    private Set<Object> createBeans(final Set<Class<?>> classes) {
        final Set<Object> beans = new HashSet<>();
        for (Class<?> aClass : classes) {
            try {
                Constructor<?> constructor = aClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                beans.add(constructor.newInstance());
            } catch (NoSuchMethodException |
                     InvocationTargetException |
                     InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return beans;
    }

    // 빈 내부에 선언된 필드를 각각 셋팅한다.
    // 각 필드에 빈을 대입(assign)한다.
    private void setFields(final Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            setField(bean, field);
        }
    }

    private void setField(Object bean, Field field) {
        for (Object injectBean : beans) {
            if (field.getType().isAssignableFrom(injectBean.getClass())
                    && field.isAnnotationPresent(Inject.class)) {
                try {
                    field.setAccessible(true);
                    field.set(bean, injectBean);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    // ClassPathScanner 클래스를 활용해보자.
    public static DIContainer createContextForPackage(final String rootPackageName) {
        return new DIContainer(ClassPathScanner.getAllClassesInPackage(rootPackageName));
    }

    // 빈 컨텍스트(DI)에서 관리하는 빈을 찾아서 반환한다.
    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        for (Object bean : beans) {
            if (aClass.isAssignableFrom(bean.getClass())) {
                return (T) bean;
            }
        }
        return null;
    }
}
