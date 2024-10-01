package di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) throws ReflectiveOperationException {
        this.beans = createBeans(classes);
        this.beans.forEach(this::setFields);
    }

    // 기본 생성자로 빈을 생성한다.
    private Set<Object> createBeans(final Set<Class<?>> classes) throws ReflectiveOperationException {
        Set<Object> beans = new HashSet<>();
        for (Class<?> aClass : classes) {
            Constructor<?> constructor = aClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            beans.add(constructor.newInstance());
        }
        return beans;
    }

    // 빈 내부에 선언된 필드를 각각 셋팅한다.
    // 각 필드에 빈을 대입(assign)한다.
    private void setFields(final Object bean) {
        try {
            Field[] fields = bean.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                field.setAccessible(true);
                Object fieldBean = getBean(field.getType());
                field.set(bean, fieldBean);
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (Object bean : beans) {
            if (aClass.isInstance(bean)) {
                return (T) bean;
            }
        } // jdbc는 이미 초기화 과정에서 넣어주니까 이거 안타게 하면 될듯?
        Constructor<?> constructor = aClass.getDeclaredConstructor(); // datasource의
        constructor.setAccessible(true);
        Object object = constructor.newInstance();
        beans.add(object);
        return (T) object;
    }
}
