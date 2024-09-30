package di.stage3.context;

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

    public DIContainer(final Set<Class<?>> classes) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        this.beans = createBeans(classes);
        for (Object bean : this.beans) {
            setFields(bean);
        }
    }

    // 기본 생성자로 빈을 생성한다.
    private Set<Object> createBeans(final Set<Class<?>> classes) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
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
    private void setFields(final Object bean) throws IllegalAccessException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            setField(bean, field);
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

    // 빈 컨텍스트(DI)에서 관리하는 빈을 찾아서 반환한다.
    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return beans.stream()
                .filter(bean -> aClass.isAssignableFrom(bean.getClass()))
                .findFirst()
                .map(bean -> (T) bean)
                .orElseThrow();
    }
}
