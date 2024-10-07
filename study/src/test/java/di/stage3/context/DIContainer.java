package di.stage3.context;

import di.ConsumerWrapper;
import di.FunctionWrapper;
import java.lang.reflect.Field;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        // 기본 생성자로 빈 생성
        this.beans = createBeans(classes);

        // 각 빈이 다른 빈과 의존성을 가지는 경우 세팅
        this.beans.forEach(bean -> setFields(bean, bean.getClass().getDeclaredFields()));
    }

    private Set<Object> createBeans(Set<Class<?>> classes) {
        return classes.stream()
                .map(FunctionWrapper.apply(clazz -> clazz.getDeclaredConstructor()))    // try-catch
                .peek(constructor -> constructor.setAccessible(true))   // UserService의 기본 생성자는 private 이므로
                .map(FunctionWrapper.apply(constructor -> constructor.newInstance()))
                .collect(Collectors.toSet());
    }

    private void setFields(final Object targetBean, final Field[] fields) {
        for (Field field : fields) {
            field.setAccessible(true);

            // 빈의 필드가 또 다른 빈인 경우
            beans.stream()
                    .filter(bean -> field.getType().isInstance(bean))
                    .forEach(ConsumerWrapper.accept(fieldBean -> field.set(targetBean, fieldBean)));
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> clazz) {
        return (T) beans.stream()
                .filter(bean -> clazz.isInstance(bean))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("no such bean exists."));
    }
}
