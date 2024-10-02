package di.stage3.context;

import di.ConsumerWrapper;
import di.FunctionWrapper;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createInstances(classes);
        this.beans.forEach(this::injectFields);
    }

    // 기본 생성자로 객체 생성
    private Set<Object> createInstances(Set<Class<?>> classes) {
        return classes.stream()
                .map(FunctionWrapper.apply(this::createNewInstance))
                .collect(Collectors.toUnmodifiableSet());
    }

    private Object createNewInstance(Class<?> clazz) throws Exception {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    // 필드 주입하기
    private void injectFields(Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        Arrays.stream(fields)
                .forEach(ConsumerWrapper.accept(field -> setField(field, bean)));
    }

    private void setField(Field field, Object bean) throws Exception {
        field.setAccessible(true);
        Class<?> fieldType = field.getType();

        if (isBeanRegistered(fieldType)) {
            field.set(bean, getBean(fieldType));
        }
    }

    private boolean isBeanRegistered(Class<?> fieldType) {
        return beans.stream()
                .anyMatch(fieldType::isInstance);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(aClass::isInstance)
                .findAny()
                .orElseThrow(() -> new IllegalStateException("bean을 찾을 수 없습니다."));
    }
}
